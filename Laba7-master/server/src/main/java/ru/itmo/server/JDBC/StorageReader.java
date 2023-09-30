package ru.itmo.server.JDBC;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StorageReader {
    private final Properties prop = new Properties();

    public StorageReader() {
        read();
    }

    private void read() {
        try(InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("storage.properties")) {
            this.prop.load(in);
        } catch (IOException e) {
            System.out.println("Произошла ошибка поиска файла настроек. Пожалуйста, проверьте наличие файла.");
        }
    }
    public String getURL() {
        return prop.getProperty("url");
    }
    public String getUserName() {
        return prop.getProperty("userName");
    }
    public String getPassword() {
        return prop.getProperty("password");
    }
    public String getDriver() {
        return prop.getProperty("driver");
    }
}
