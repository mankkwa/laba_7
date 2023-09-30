package ru.itmo.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;

/**
 * Класс для получения логина и пароля пользователя
 */
public class User {
    private String password;
    private String username;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Метод для хеширования
     * @param word слово для хеширования
     * @return хешированное слово по стандарту SHA-224
     */
    public static String getHash(String word) {
        MessageDigest mD = null;
        try {
            mD = MessageDigest.getInstance("SHA-224");
        } catch (Exception e) {
            System.err.println("Проблемы с хешированием данных.");
        }
        assert mD != null;
        byte[] hash = mD.digest(word.getBytes(StandardCharsets.UTF_8));
        return Arrays.toString(hash);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(password, user.password) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, username);
    }
}
