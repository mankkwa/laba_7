package ru.itmo.client.service;

import ru.itmo.client.handlers.InputHandler;
import ru.itmo.common.User;
import ru.itmo.common.exceptions.TypeOfError;
import ru.itmo.common.exceptions.WrongArgumentException;
import ru.itmo.common.messages.MessageManager;

import java.sql.Array;
import java.util.Arrays;

public class Authorization {
    private final InputHandler in;
    private final MessageManager msg;
    private String savedString;

    public Authorization(InputHandler in, MessageManager msg) {
        this.in = in;
        this.msg = msg;
    }

    public String getSavedString() {
        return savedString;
    }

    private String askSmth(String something) {
        String result;
        while (true) {
            try {
                msg.printMessage(something);
                System.out.print("> ");
                result = in.readInput();
                if (result.equals("")) throw new WrongArgumentException(TypeOfError.EMPTY);
                break;
            } catch (WrongArgumentException e) {
                msg.printErrorMessage(e);
            } catch (Exception e) {
                System.err.print("Ошибо4ка...\n");
            }
        }
        return result;
    }

    public String askLogin() {
        return askSmth("логин");
    }

    public String askPassword(){
        return askSmth("пароль");
    }

    /**
     * Метод для проверки пароля
     * @param password введенный пароль
     * @return сравнение хешей введенного пароля с предполагаемым
     */
    public boolean checkPassword(String password){
        savedString = askPassword();
        String hashed = User.getHash(savedString);
        return hashed.equals(password);
    }

    /**
     * Метод для проверки, совпадает ли введенный логин с каким-либо из списка
     * @param login массив логинов в таблице пользователей
     * @return есть ли среди логинов введенный (true - да, false - нет)
     */
    public boolean checkLogin(Array login){
        savedString = askLogin();
        return Arrays.asList(login).contains(savedString);
    }
}
