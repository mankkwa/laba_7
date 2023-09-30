package ru.itmo.server.utility;

import ru.itmo.common.User;
import ru.itmo.server.JDBC.JdbcManager;

import java.sql.*;

public class HandleUsers {
    private final String COLLECTION_NAME = "USERS";
    private final String ADD_COMMAND = "INSERT INTO "+ COLLECTION_NAME;
    private final String GET_LOGIN_COMMAND = "SELECT login FROM " + COLLECTION_NAME;
    private final String GET_PASSWORD_COMMAND = "SELECT password FROM " + COLLECTION_NAME + " WHERE login = '";
    private static Connection connection;

    public void setConnection() {
        connection = JdbcManager.connectToDataBase();
    }

    public void add(String name, String passwrd){
        String hash = User.getHash(passwrd);
        String sql = ADD_COMMAND + "(login, password) VALUES " +
                "('" + name + "', '" + hash + "');";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Произошла ошибка с добавлением нового пользователя.");
        }
    }

    public String getPassword(String name){
        String result;
        String sql = GET_PASSWORD_COMMAND + name + "';";
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            result = resultSet.getString("password");
        } catch (SQLException e) {
            System.err.println("Пользователя с данным паролем не существует.");
            return null;
        }
        return result;
    }

    public String getLogin(String name){
        String result;
        String sql = GET_LOGIN_COMMAND + " WHERE login = '" + name + "';";
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            result = resultSet.getString("login");
        } catch (SQLException e) {
            System.err.println("Пользователя с данным логином не существует.");
            return null;
        }
        return result;
    }

    public Array getLogins(){
        Array result = null;
        String sql = GET_LOGIN_COMMAND;
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            result = resultSet.getArray("login");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
