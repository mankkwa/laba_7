package ru.itmo.client;

import ru.itmo.client.service.AskInput;
import ru.itmo.client.service.ReaderManager;
import ru.itmo.client.to_server.ServerAPI;
import ru.itmo.client.to_server.ServerAPIImpl;
import ru.itmo.common.User;
import ru.itmo.common.commands.CommandType;
import ru.itmo.common.exceptions.TypeOfError;
import ru.itmo.common.exceptions.WrongArgumentException;
import ru.itmo.common.messages.MessageManager;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.responses.Response;

/**
 * Класс, содержащий основную логику работы клиента
 */
public class Client {
    private final MessageManager msg = new MessageManager();
    private final AskInput ask = new AskInput();
    private final String serverHost;
    private final int serverPort;
    private final int connectionAttempts = 20;
    private final int connectionTimeout = 2000;

    public Client(String serverHost, int serverPort){

        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void start() {
        ServerAPI serverAPI = new ServerAPIImpl(serverHost, serverPort);
        boolean run = true;

        while (!serverAPI.connectToServer()) {
            if(serverAPI.getAttempts() > connectionAttempts){
                System.err.println("Превышено количество попыток подключиться");
                return;
            }
            try {
                Thread.sleep(connectionTimeout);
            } catch (InterruptedException e) {
                System.err.println("Произошла ошибка при попытке ожидания подключения!");
                System.err.println("Повторное подключение будет произведено немедленно.");
            }
        }

        //вот тут происходит авторизация
        User user = null;
        while (user == null) {
            user = userProcessing(serverAPI);
        }

        while(run) {
            try {
                CommandType commandType = ask.askCommand(ReaderManager.getHandler());
                HumanBeing human = ask.askInputManager(commandType, ReaderManager.getHandler());

                Response response = serverAPI.executeCommand(commandType, human, user);
                if(response.status == Response.Status.OK) {
                    if(response.getArgumentAs(String.class) != null) {
                        System.out.println(response.getArgumentAs(String.class));
                    }
                } else if(response.status == Response.Status.SERVER_EXIT) {
                    System.out.println("Клиент завершает свою работу.");
                    serverAPI.closeConnection();
                    run = false;
                } else if(response.status == Response.Status.ERROR) {
                    System.err.println("В процессе выполнения данной команды произошла ошибка.");
                } else if(response.status == Response.Status.WARNING) {
                    System.out.println("Внимание! "+response.getArgumentAs(String.class));
                }
            } catch (NullPointerException e) {
                ReaderManager.returnOnPreviousReader();
                ask.removeLastElement();
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.err.println("Непредвиденная ошибка.");
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
                if (e.getType() == TypeOfError.CONNECTION_BROKEN) {

                    System.out.println("Попытка переподключиться..");
                    while (!serverAPI.connectToServer()){
                        if(serverAPI.getAttempts() > connectionAttempts){
                            System.err.println("Превышено количество попыток подключиться.");
                            return;
                        }
                        try {
                            Thread.sleep(connectionTimeout);
                        } catch (InterruptedException exception) {
                            System.err.println("Произошла ошибка при попытке ожидания подключения.");
                        }
                    }

                }
                if (e.getType() == TypeOfError.CONNECTED_REFUSE) {
                    run = false;
                }
                if(e.getType() == TypeOfError.NOT_STARTED) {
                    run = false;
                }
            }

        }
    }

    /**
     * Регистрация клиента
     * @return авторизованный клиент
     */
    private User userProcessing(ServerAPI serverAPI){
        User user = null;

        Boolean isAuthorized = null;
        while (isAuthorized == null){
            isAuthorized = ask.askAuthorization(ReaderManager.getHandler());
        }

        if (isAuthorized) {
            //ввод логина
            String login = ask.askLogin(ReaderManager.getHandler());
            //ввод пароля
            String password = ask.askPassword(ReaderManager.getHandler());
            //проверка на наличие такого юзера
            user = new User(login, password);
            User user1 = checkUser(serverAPI, user);

            //обработка статуса
            if (user1 != null) {
                if (user1.getUsername() == null) {
                    System.err.println("Аккаунт с данными логином и паролем не найден. Пожалуйста, проверьте правильность ввода и попробуйте еще раз.");
                    return null;
                } else if (user1.getPassword() == null){
                    System.err.println("Пароль введен неверно.");
                    return null;
                } else {
                    return user;
                }
            }
        } else {
            user = newLogin(serverAPI);
        }
        return user;
    }

    /**
     * Метод для проверки наличия логина в базе существующих логинов пользователей
     * @return пользователя (если логин уже есть) или null (если такого логина среди созданных нет)
     * @throws WrongArgumentException
     */
    private User checkUser(ServerAPI serverAPI, User user) {
        try {
            Response response = serverAPI.executeCommand(CommandType.CHECK_USER, null, user);
            return response.getUser();
        } catch (WrongArgumentException e) {
            msg.printErrorMessage(e);
        }
        return null;
    }

    private User newLogin(ServerAPI serverAPI) {
        String login = ask.askLogin(ReaderManager.getHandler());
        String password = ask.askPassword(ReaderManager.getHandler());
        User user = new User(login, password);
        User user1 = registration(serverAPI, user);
        if (user1 != null) {
            if (user1.getUsername() != null) {
                System.err.println("Такой логин уже существует. Придумайте другой.");
                newLogin(serverAPI);
                return null;
            }
        } else {
            System.err.println("Произошла какая-то ошибка.");
            return null;
        }
        return user;
    }

    private User registration(ServerAPI serverAPI, User user) {
        try {
            Response response = serverAPI.executeCommand(CommandType.REGISTRATION, null, user);
            return response.getUser();
        } catch (WrongArgumentException e) {
            msg.printErrorMessage(e);
        }
        return null;
    }
}
