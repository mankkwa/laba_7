package ru.itmo.client.service;

import ru.itmo.client.handlers.ConsoleInputHandler;
import ru.itmo.client.handlers.FileInputHandler;
import ru.itmo.client.handlers.InputHandler;
import ru.itmo.common.messages.MessageManager;

import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Класс, отвечающий за переключение типа считывания и
 * отслеживания истории вызываемых классов считывания
 */
public class ReaderManager {
    private static InputHandler handler;
    private static final ArrayList<InputHandler> handlers = new ArrayList<>();

    public static InputHandler getHandler() {
        return handler;
    }

    /**
     * Меняет тип считывания на считывание с консоли
     */
    public static void turnOnConsole() {
        // новый экземпляр класса считывания
        handler = new ConsoleInputHandler();
        // добавляем в массив хендлеров, чтобы потом к нему вернуться
        handlers.add(handler);
        // Возврат к дружественному интерфейсу после считывания с файла, если оно было
        MessageManager.returnFriendly();
        // Добавляем косоль в список активных вкладок приложения
        MessageManager.getFileHistory().add("Console");
    }

    /**
     * Возврат к предыдущему типы считывания
     */
    public static void returnOnPreviousReader() {
        handlers.remove(handlers.size() - 1);
        handler = handlers.get(handlers.size()-1);
        MessageManager.returnFriendly();
    }

    /**
     * Меняет тип считывания на считывание с файла.
     * Отключает дружественный интерфейс, если он включён
     */
    public static void turnOnFile(BufferedReader reader) {
        handler = new FileInputHandler(reader);
        handlers.add(handler);
        MessageManager.turnOffFriendly();
    }
}
