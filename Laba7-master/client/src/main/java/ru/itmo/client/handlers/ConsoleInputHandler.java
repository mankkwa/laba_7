package ru.itmo.client.handlers;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс для считывания данных с консоли
 */
public class ConsoleInputHandler extends InputHandler{
    private final Scanner scanner = new Scanner(System.in);
    /**
     * Переопределённый метод для считывания с консоли
     * @return первое слово считанной строки без лишних пробелов
     */
    @Override
    public String readInput() {
        try {
            return scanner.nextLine().trim().split(" ")[0];
        } catch(NoSuchElementException e) {
            System.out.print("Куда ты жмал?? Программа завершает свою работу без сохранения данных.\n");
            System.exit(0);
        }
        return null;
    }
}
