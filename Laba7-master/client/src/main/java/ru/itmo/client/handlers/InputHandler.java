package ru.itmo.client.handlers;

import java.io.IOException;

/**
 * Абстрактный класс-родитель для классов с разными типами считывания
  */
public abstract class InputHandler {
    public abstract String readInput() throws IOException;
}
