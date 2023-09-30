package ru.itmo.client.service;

import ru.itmo.client.handlers.InputHandler;
import ru.itmo.common.User;
import ru.itmo.common.commands.CommandType;
import ru.itmo.common.exceptions.*;
import ru.itmo.common.messages.MessageManager;
import ru.itmo.common.model.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.sql.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * Класс, запрашивающий пользовательский ввод и валидирующих полученные значения
 * Работает для всех типов считывания
 */
public class AskInput {
    private final MessageManager msg = new MessageManager();

    public void removeLastElement() {
        MessageManager.getFileHistory().remove(
                MessageManager.getFileHistory().size()-1
        );
    }
    /**
     * Создает новый экземпляр класса HumanBeing с пустыми полями. Проходится по полям класса, и если поле класса
     * соответствует полю, запрашиваемому в данной команде, то происходит вставка запрошенного значения
     * @param in
     */
    public HumanBeing askInputManager(CommandType commandType, InputHandler in) throws WrongArgumentException{
        HumanBeing newHuman = new HumanBeing();
        try {
            // итератор для перемещения по нужным для команды методам
            Iterator<String> iterator = Arrays.stream(commandType.getCommandFields()).iterator();
            if(iterator.hasNext()) {
                // название нужного для запроса поля в массиве энама выбранной команды
                String commandName = iterator.next();
                // цикл foreach для полей newHuman
                for (Field fields : newHuman.getClass().getDeclaredFields()) {
                    // название нынешнего поля newHuman
                    String fieldName = fields.getName().toLowerCase();
                    // если поле массива энама команды совпадает с перебираемым полем экземпляра newHuman
                    if (fieldName.equals(commandName.substring(3).toLowerCase())) {
                        // беру ссылку на необходимый для запроса метод
                        Method method = this.getClass().getDeclaredMethod(commandName, InputHandler.class);
                        // ставлю разрешение на использование метода
                        method.setAccessible(true);
                        // вызываю нужный метод и получаю уже проверенное введенное значение
                        Object o = method.invoke(this, in);
                        // ставлю разрешение на изменение приватного поля newHuman
                        fields.setAccessible(true);
                        // изменяю значение приватного поля
                        fields.set(newHuman, o);
                        // перехожу к следующему необходимому для команды полю
                        if(iterator.hasNext()) commandName = iterator.next();
                        else break;
                    }
                }
                if(Objects.equals(commandName, "askFileName")) {
                    ReaderManager.turnOnFile(askFileName(in));
                    throw new WrongArgumentException(TypeOfError.SWITCH_READER);
                }
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            // здесь считаю строки
            throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
        }
        newHuman.setCreationDate(LocalDate.now());
        return newHuman;
    }

    /**
     * Метод, спрашивающий, есть ли у пользователя аккаунт
     * @param in InputHandler
     * @return true (если аккаунт уже есть), false (если его нет)
     */
    public Boolean askAuthorization(InputHandler in){
        String result;
        Boolean booleanResult = null;
        System.out.print("Есть ли у Вас аккаунт?\n");
        try {
            result = in.readInput();
            if (result.equals("")) {
                System.err.println("Пожалуйста, введите буквы с клавиатуры.");
                return null;
            }
            booleanResult = toBoolean(result, false);
        } catch (IOException | WrongArgumentException e) {
            System.err.println("Вы что ввели такое....");
        }
        return booleanResult;
    }

    public String askLogin(InputHandler in){
        Authorization auth = new Authorization(in, msg);
        return auth.askLogin();
    }

    public String askPassword(InputHandler in){
        Authorization auth = new Authorization(in, msg);
        return auth.askPassword();
    }

    /**
     * Запрашивает ввод команды и валидирует введённую пользователем строку
     * @param in - тип считывания (с консоли или с файла)
     * @return индекс команды, если она была найдена - иначе запрашивает повторный ввод
     */
    public CommandType askCommand(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("команду");
            try {
                return isCorrectCommand(in.readInput());
            } catch (IOException e) {
                msg.printErrorMessageIO();
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            }
        } while (MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_COMMAND);
    }

    /**
     * Следующие методы запрашивают ввод полей и валидируют введённое пользователем значение
     * @param in - тип считывания (с консоли или с файла)
     * @return поле, если оно было введено верно
     */
    private Integer askId(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("id");
            try {
                return isCorrectInteger(in.readInput(), 0);
            } catch(IOException e) {
                msg.printErrorMessageIO();
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            }
        } while(MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }

    private String askName(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("имя");
            try {
                return isCorrectString(in.readInput());
            } catch (IOException e) {
                msg.printErrorMessageIO();
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            }
        } while (MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }

    private String askSoundtrackName(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("саундтрек");
            try {
                return isCorrectString(in.readInput());
            } catch (IOException e) {
                msg.printErrorMessageIO();
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            }
        } while (MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }

    private Long askMinutesOfWaiting(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("минуты");
            try {
                return isCorrectLong(in.readInput(), -1);
            } catch (IOException e) {
                msg.printErrorMessageIO();
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            }
        } while (MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }

    private int askImpactSpeed(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("скорость");
            try {
                return isCorrectInteger(in.readInput(), -1);
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            } catch (IOException e) {
                msg.printErrorMessageIO();
            }
        } while(MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }

    private Boolean askRealHero(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("статус геройства");
            try {
                return toBoolean(in.readInput(), false);
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            } catch (IOException e) {
                msg.printErrorMessageIO();
            }
        } while (MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }

    private Boolean askHasToothpick(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("наличие зубочистки");
            try {
                return toBoolean(in.readInput(), true);
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            } catch (IOException e) {
                msg.printErrorMessageIO();
            }
        } while (MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }

    private Coordinates askCoordinates(InputHandler in) throws WrongArgumentException {
        msg.printMessage("местоположение");
        return new Coordinates(askX(in), askY(in));
    }
    private int askX(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("координату x");
            try {
                return isCorrectInteger(in.readInput());
            } catch(WrongArgumentException e) {
                msg.printErrorMessage(e);
            } catch (IOException e) {
                msg.printErrorMessageIO();
            }
        } while(MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }
    private Float askY(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("координату y");
            try {
                return isCorrectFloat(in.readInput(), -188);
            } catch(WrongArgumentException e) {
                msg.printErrorMessage(e);
            } catch (IOException e) {
                msg.printErrorMessageIO();
            }
        }
        while(MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }

    private Mood askMood(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("состояние");
            try {
                return isCorrectMood(in.readInput());
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            } catch (IOException e) {
                msg.printErrorMessageIO();
            }
        }
        while (MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }

    private Car askCar(InputHandler in) throws WrongArgumentException {
        msg.printMessage("данные о машине персонажа");
        return new Car(askCarName(in), isCool(in));
    }
    private String askCarName(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("название машины");
            try {
                String input = in.readInput();
                if(input.isEmpty()) {
                    msg.printWarningMessage();
                    return null;
                } return input;
            } catch (IOException e) {
                msg.printErrorMessageIO();
            }
        }
        while(MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }
    private boolean isCool(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("степень крутости машины");
            try {
                return toBoolean(in.readInput(), false);
            } catch(WrongArgumentException e) {
                msg.printErrorMessage(e);
            } catch (IOException e) {
                msg.printErrorMessageIO();
            }
        } while(MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }
    private BufferedReader askFileName(InputHandler in) throws WrongArgumentException {
        do {
            msg.printMessage("путь до файла");
            try {
                String fileName = in.readInput();
                BufferedReader reader = new BufferedReader(isCorrectFile(fileName));
                MessageManager.getFileHistory().add(fileName);
                return reader;
            } catch (IOException e) {
                msg.printErrorMessageIO();
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            }
        }
        while(MessageManager.isFriendlyInterface());
        throw new WrongArgumentException(TypeOfError.IGNORE_STRING);
    }

    /**
     * Внутренние вспомогательные методы для упрощения проверки введённых данных
     * @param input строка, введённая пользователем
     * @return true (если в строке присутствует true, yes, да вне зависимости от регистра),
     * false (если в строке присутствует false, no, нет или если строка пустая)
     */
    private Boolean toBoolean(String input, boolean hasNull) throws WrongArgumentException {
        input = input.toLowerCase();
        if (input.equals("true") || input.equals("yes") || input.equals("да")) {
            return true;
        } else if (input.equals("false") || input.equals("no") || input.equals("нет")) {
            return false;
        } else if (input.isEmpty() && !hasNull) {
            throw new WrongArgumentException(TypeOfError.EMPTY);
        } else if(input.isEmpty()) {
            msg.printWarningMessage();
            return null;
        }
        else {
            throw new WrongArgumentException(TypeOfError.UNKNOWN);
        }
    }

    private String isCorrectString(String input) throws WrongArgumentException {
        if(input.isEmpty()) throw new WrongArgumentException(TypeOfError.EMPTY);
        return input;
    }

    /**
     * Функция для проверки валидности введённого целочисленного значения
     * @param input - строка, введённая пользователем
     * @return если строка валидна - возращает целое число, иначе выбрасывает следующее исключение
     */
    private Integer isCorrectInteger(String input) throws WrongArgumentException {
        try {
            Integer.parseInt(input);
        } catch (IllegalArgumentException e) {
            if(input.isEmpty()) throw new WrongArgumentException(TypeOfError.EMPTY);
            else throw new WrongArgumentException(TypeOfError.WRONG_TYPE);
        }
        return Integer.parseInt(input);
    }

    /**
     * Функция для проверки валидности введённого целого числа с установкой нижней границы
     * @param input - строка, введённая пользователем
     * @param begin - нижняя граница для данного поля
     * @return если строка валидна - возращает целое число, иначе выбрасывает следующее исключение
     */
    private Integer isCorrectInteger(String input, int begin) throws WrongArgumentException {
        try {
            if (Integer.parseInt(input) <= begin) {
                throw new WrongArgumentException(TypeOfError.OUT_OF_RANGE);
            }
        } catch (IllegalArgumentException e) {
            if(input.isEmpty()) throw new WrongArgumentException(TypeOfError.EMPTY);
            else throw new WrongArgumentException(TypeOfError.WRONG_TYPE);
        }
        return Integer.parseInt(input);
    }

    /**
     * Функция для проверки валидности введённого целого числа с установкой нижней границы
     * @param input - строка, введённая пользователем
     * @param begin - нижняя граница для данного поля
     * @return если строка валидна - возращает целое число, иначе выбрасывает следующее исключение
     */
    private Long isCorrectLong(String input, int begin) throws WrongArgumentException {
        try {
            if (Integer.parseInt(input) <= begin) {
                throw new WrongArgumentException(TypeOfError.OUT_OF_RANGE);
            }
        } catch (IllegalArgumentException e) {
            if(input.isEmpty()) throw new WrongArgumentException(TypeOfError.EMPTY);
            else throw new WrongArgumentException(TypeOfError.WRONG_TYPE);
        }
        return Long.parseLong(input);
    }

    /**
     * Функция для проверки валидности введённого дробного числа с установкой нижней границы
     * @param input - строка, введённая пользователем
     * @param begin - нижняя граница для данного поля
     * @return если строка валидна - возращает целое число, иначе выбрасывает следующее исключение
     */
    private Float isCorrectFloat(String input, int begin) throws WrongArgumentException {
        try {
            if (Float.parseFloat(input) <= begin) {
                throw new WrongArgumentException(TypeOfError.OUT_OF_RANGE);
            }
        } catch (IllegalArgumentException e) {
            if(input.isEmpty()) throw new WrongArgumentException(TypeOfError.EMPTY);
            else throw new WrongArgumentException(TypeOfError.WRONG_TYPE);
        }
        return Float.parseFloat(input);
    }

    private CommandType isCorrectCommand(String input) throws WrongArgumentException {
        try {
            return CommandType.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            if(input.isEmpty()) throw new WrongArgumentException(TypeOfError.EMPTY);
            throw new WrongArgumentException(TypeOfError.UNKNOWN);
        }
    }

    private Mood isCorrectMood(String input) throws WrongArgumentException {
        try {
            return Mood.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            if(input.isEmpty()) {
                msg.printWarningMessage();
                return null;
            }
            else throw new WrongArgumentException(TypeOfError.UNKNOWN);
        }
    }

    private FileReader isCorrectFile(String input) throws WrongArgumentException{
        try {
            if(MessageManager.getFileHistory().contains(input)) {
                throw new WrongArgumentException(TypeOfError.ALREADY_EXECUTED);
            }
            return new FileReader(input);
        } catch (FileNotFoundException e) {
            throw new WrongArgumentException(TypeOfError.NOT_FOUND);
        }
    }
}