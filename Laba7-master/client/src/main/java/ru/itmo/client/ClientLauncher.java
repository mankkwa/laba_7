package ru.itmo.client;

import ru.itmo.client.service.ReaderManager;
import ru.itmo.common.messages.MessageManager;

public class ClientLauncher {
    private static String ServerHost;
    private static int ServerPort;

    public static void main(String[] args) {
        try {
            ServerHost = "localhost";
            ServerPort = 65100;
            System.out.println("Получены хост: " + ServerHost + " и порт: " + ServerPort);
        } catch (NumberFormatException exception){
            System.err.println("Порт должен быть числом.");
            return;
        } catch (ArrayIndexOutOfBoundsException exception){
            System.err.println("Недостаточно аргументов.");
            return;
        }
        Client client = new Client(ServerHost, ServerPort);
        ReaderManager.turnOnConsole();
        new MessageManager().turnOnFriendly();
        client.start();
    }
}
