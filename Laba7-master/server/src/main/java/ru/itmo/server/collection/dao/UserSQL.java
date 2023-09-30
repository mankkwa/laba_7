package ru.itmo.server.collection.dao;

import ru.itmo.common.User;
import ru.itmo.server.JDBC.JdbcManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserSQL {
    Connection connection = JdbcManager.connectToDataBase();

    public Boolean add(User user) {
        String sql = "INSERT INTO USERS (login, password) VALUES ('" +
                user.getUsername() + "', '" +
                User.getHash(user.getPassword()) + "')";
        return sendToDataBaseUpdate(sql);
    }

    private Boolean sendToDataBaseUpdate(String sql) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public Boolean search(User user) {
        String sql = "SELECT login, password FROM USERS WHERE login = '" +user.getUsername()+ "' AND " +
                " password = '" +User.getHash(user.getPassword()) + "'";
        ResultSet result = sendToDataBaseQuery(sql);
        try {
            result.next();
                String login = result.getString("login");
                String password = result.getString("password");
                return true;
        } catch(SQLException | NullPointerException e) {
            System.err.println("Бля");
            return false;
        }
    }

    private ResultSet sendToDataBaseQuery(String sql) {
        ResultSet result = null;
        try {
            Statement stmt = connection.createStatement();
            result = stmt.executeQuery(sql);
        } catch(SQLException e) {
            System.out.println("Случилась хуета");
        }
        return result;
    }
}
