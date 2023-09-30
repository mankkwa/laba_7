package ru.itmo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.itmo.server.JDBC.JdbcManager;

import java.sql.SQLException;

public class ServerLauncher {

    private static String host;
    private static int port;
    public static final Logger log = LogManager.getLogger(ServerLauncher.class.getName());

    public static void main(String[] args) {
        try {
            host = "localhost";
            port = 65100;
            ServerLauncher.log.info("Получены хост: " + host + " и порт: " + port);
        } catch (NumberFormatException exception){
            ServerLauncher.log.fatal("Порт должен быть числом.");
            return;
        } catch (ArrayIndexOutOfBoundsException exception){
            ServerLauncher.log.fatal("Недостаточно аргументов, проверьте их наличие в настройках конфигурации.");
            return;
        }
        Server server = new Server(host, port);
        server.start();
    }
}
