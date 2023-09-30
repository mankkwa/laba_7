package ru.itmo.client.handlers;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Класс для считывания данных с файла
 */
public class FileInputHandler extends InputHandler{
    private final BufferedReader reader;
    public FileInputHandler(BufferedReader reader) {
        this.reader = reader;
    }
    /**
     * Переопределённый метод для считывания с консоли
     * @return первое слово считанной строки без лишних пробелов
     */
    @Override
    public String readInput() throws IOException {
        return reader.readLine().trim().split(" ")[0];
    }
}
