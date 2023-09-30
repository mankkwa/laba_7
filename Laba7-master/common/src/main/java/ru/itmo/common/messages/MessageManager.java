package ru.itmo.common.messages;

import ru.itmo.common.exceptions.TypeOfError;
import ru.itmo.common.exceptions.WrongArgumentException;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MessageManager {
    private static boolean CONST_FRIENDLY_INTERFACE;
    private static boolean friendlyInterface;
    private final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<String> fileHistory = new ArrayList<>();

    public static ArrayList<String> getFileHistory() {
        return fileHistory;
    }

    /**
     * Включает дружественный интерфейс и запоминает изначальную его настройку
     */
    public void turnOnFriendly() {
        System.out.println("Включить дружественный интерфейс?");
        try {
            String answer = scanner.nextLine().trim()
                    .split(" ")[0].toLowerCase();
            if(answer.isEmpty()) {
                CONST_FRIENDLY_INTERFACE = false;
            } else {
                CONST_FRIENDLY_INTERFACE = true;
            }
            friendlyInterface = CONST_FRIENDLY_INTERFACE;
        } catch (NoSuchElementException e) {
            System.err.println("До связи... Программа завершает свою работу");
            System.exit(0);
        }
    }

    /**
     * Выключает дружественный интерфейс
     */
    public static void turnOffFriendly() {
        friendlyInterface = false;
    }

    /**
     * Возвращает к изначальной настройке дружественного интерфейса
     */
    public static void returnFriendly() {
        friendlyInterface = CONST_FRIENDLY_INTERFACE;
    }

    public static boolean isFriendlyInterface() {
        return friendlyInterface;
    }

    /**
     * Внутренний метод для вывода сообщения относительно friendlyInterface
     */
    public void printMessage(String name){
        if (friendlyInterface) {
            System.out.println("Введите "+name+":");
        }
    }

    /**
     * Выводит сообщение, объясняющее природу вызванного исключения
     * @param e - исключение
     */
    public void printErrorMessage(WrongArgumentException e) {
        if(e.getType() == TypeOfError.SWITCH_READER && fileHistory.size() == 2)  {
            printWarningMessage(e);
        } else if(e.getType() != TypeOfError.SWITCH_READER) {
            System.err.println("\u001B[37m"+fileHistory.get(fileHistory.size() - 1)
                    +": \u001B[0m"+e.getType().getDescription());
        }
    }

    public void printErrorMessageIO() {
        System.err.println("Ошибка чтения данных из файла");
    }

    public void printWarningMessage() {
        if(friendlyInterface) {
            System.out.println("\u001B[33mВы ввели пустую строку. Поле примет значение null.\u001B[0m");
        }
    }
    public void printWarningMessage(WrongArgumentException e) {
        System.out.println("\u001B[33m"+e.getType().getDescription()+"\u001B[0m");
    }
}
