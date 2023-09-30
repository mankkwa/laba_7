package ru.itmo.server.collection.dao;

import ru.itmo.common.User;
import ru.itmo.common.model.Car;
import ru.itmo.common.model.Coordinates;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.model.Mood;
import ru.itmo.server.JDBC.JdbcManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PostgreSqlDao implements DAO{
    private static Connection connection;
    private final String COLLECTION_NAME = "HUMAN_BEING_COLLECTION";
    private final String ADD_COMMAND = "INSERT INTO "+ COLLECTION_NAME;
    private final String UPDATE_COMMAND = "UPDATE " + COLLECTION_NAME +" SET ";
    private final String DELETE_COMMAND = "DELETE FROM " + COLLECTION_NAME;
    private final String GET_COMMAND = "SELECT * FROM " + COLLECTION_NAME + " WHERE ";
    private final String GET_ID = "SELECT ID FROM " + COLLECTION_NAME;

    public static void setConnection() {
        connection = JdbcManager.connectToDataBase();
    }
    public static Connection getConnection() {
        return connection;
    }
    /**
     * override DAO methods
     */
    @Override
    public int add(HumanBeing humanBeing, User user) {
        int id = 0;
        String sql = ADD_COMMAND + "(creationDate, name, soundtrackName, minutesOfWaiting," +
                " impactSpeed, realHero, hasToothpick, coordinates, mood, car,  login) VALUES (" +
                "TO_DATE('"+humanBeing.getCreationDate().toString() + "', 'YYYY/MM/DD'), '" +
                humanBeing.getName() + "', '" +
                humanBeing.getSoundtrackName() + "', '" +
                humanBeing.getMinutesOfWaiting() + "', '" +
                humanBeing.getImpactSpeed() + "', '" +
                humanBeing.isRealHero() + "', " +
                convertToSQL(humanBeing.isHasToothpick()) + ", '(" +
                humanBeing.getCoordinates().getX() +", " + humanBeing.getCoordinates().getY()+ ")', " +
                convertToSQL(humanBeing.getMood()) + ", '(" +
                humanBeing.getCar().getCarName() + ", " + humanBeing.getCar().getCarCool() + ")', " +
                "'" + user.getUsername() + "') " +
                "RETURNING id";
        try {
            ResultSet result = sendToDataBaseQuery(sql);
            result.next();
            id = result.getInt("id");
        } catch (SQLException e) {
            id = -1;
        }
        return id;
    }
    @Override
    public boolean update(HumanBeing humanBeing, User user) {
        String sql = null;
        if(getSQL("id = ", humanBeing.getId()) != null) {
            sql = UPDATE_COMMAND + "name = '" + humanBeing.getName() + "' ," +
                    "soundtrackName = '" + humanBeing.getSoundtrackName() + "' ," +
                    "minutesOfWaiting = " + humanBeing.getMinutesOfWaiting() + " ," +
                    "impactSpeed = " + humanBeing.getImpactSpeed() + " ," +
                    "realHero = '" + humanBeing.isRealHero() + "' ," +
                    "hasToothpick = " + convertToSQL(humanBeing.isHasToothpick()) + ", " +
                    "coordinates = '(" + humanBeing.getCoordinates().getX() + ", " +
                    humanBeing.getCoordinates().getY() + ")', " +
                    "mood = " + convertToSQL(humanBeing.getMood()) + ", " +
                    "car = '(" + humanBeing.getCar().getCarName() + ", " +
                    humanBeing.getCar().getCarCool() + ")' WHERE " +
                    "id = " + humanBeing.getId() + " AND login = '" + user.getUsername() + "'";
        }
        return sendToDataBaseUpdate(sql);
    }

    @Override
    public boolean delete(int index, User user) {
        String sql = null;
        if(get(index) != null) {
            sql = DELETE_COMMAND + " WHERE " +
                    "id = " + index + " AND login = '" + user.getUsername() + "'";
        }
        return sendToDataBaseUpdate(sql);
    }

    public ArrayList<String> getSQL(String column, Object obj) {
        String sql = GET_COMMAND + column + obj.toString();
        ResultSet result = sendToDataBaseQuery(sql);
        ArrayList<String> rows = new ArrayList<>();
        String row = null;
        try {
            while (result.next()) {
                row = "id = " + result.getInt("id") +"\n"+
                        "creationDate = " + result.getDate("creationDate") +"\n"+
                        "name = " + result.getString("name") +"\n"+
                        "soundtrackName = " + result.getString("soundtrackName") +"\n"+
                        "minutesOfWaiting = " + result.getInt("minutesOfWaiting") +"\n"+
                        "impactSpeed = " + result.getInt("impactSpeed") +"\n"+
                        "realHero = " + result.getBoolean("realHero") +"\n"+
                        "hasToothpick = " + result.getString("hasToothpick") +"\n"+
                        "coordinates = " + result.getObject("coordinates") +"\n"+
                        "mood = " + result.getObject("mood") +"\n"+
                        "car = " + result.getObject("car") +"\n"+
                        "login = " + result.getString("login");
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Случилась еще одна хуета");
        }
        return rows;
    }

    public Deque<HumanBeing> getAll() {
        Deque<HumanBeing> humanCollection = new ConcurrentLinkedDeque<>();
        ResultSet result = sendToDataBaseQuery(GET_ID);
        try {
            if (result!=null) {
                while (result.next()) {
                    int index = result.getInt("id");
                    humanCollection.add(get(index));
                }
            }
        } catch (SQLException e) {
            System.out.println("Бляяяяя проблема появилась :(");
        }
        return humanCollection;
    }

    public HumanBeing get(int id) {
        String sql = GET_COMMAND + "id = " + id;
        ResultSet result = sendToDataBaseQuery(sql);
        HumanBeing human = null;
        try {
            while (result.next()) {
                human = new HumanBeing(
                         result.getString("name"),
                         result.getString("soundtrackName"),
                         result.getLong("minutesOfWaiting"),
                         result.getInt("impactSpeed"),
                         result.getBoolean("realHero"),
                         result.getBoolean("hasToothpick"),
                         getCoordinates(result.getObject("coordinates")),
                         getMood(result.getObject("mood")),
                         getCar(result.getObject("car")));
                human.setId(result.getInt("id"));
                human.setCreationDate(result.getDate("creationDate").toLocalDate());
                human.setUserLogin(result.getString("login"));
            }
        } catch (SQLException e) {
            System.out.println("Случилась еще одна хуета");
        }
        return human;
    }

    private Coordinates getCoordinates(Object obj) {
        String arg = obj.toString();
        int x = Integer.parseInt(arg.substring(arg.indexOf("(")+1, arg.indexOf(",")));
        float y = Float.parseFloat(arg.substring(arg.indexOf(",")+1, arg.indexOf(")")));
        return new Coordinates(x, y);
    }

    private Mood getMood(Object obj) {
        if(obj == null) return null;
        return Mood.valueOf(obj.toString());
    }

    private Car getCar(Object obj) {
        String arg = obj.toString();
        String substring = arg.substring(arg.indexOf("(") + 1, arg.indexOf(","));
        String carName = Objects.equals(substring, "null") ?
                null : substring;
        boolean isCool = Objects.equals(arg.substring(arg.indexOf(",") + 1, arg.indexOf(")")), "true");
        return new Car(carName, isCool);
    }
    public ArrayList<Integer> getAllSQL() {
        ArrayList<Integer> indexes = new ArrayList<>();
        String sql = GET_ID;
        ResultSet result = sendToDataBaseQuery(sql);
        try {
            while(result.next()) {
                int index = result.getInt("id");
                indexes.add(index);
            }
        } catch (SQLException e) {
            System.out.println("Бляяяяя проблема появилась :(");
        }
        return indexes;
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

    private boolean sendToDataBaseUpdate(String sql) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch(SQLException | NullPointerException e) {
            return false;
        }
        return true;
    }
    private String convertToSQL(Boolean boolValue) {
        return (boolValue == null) ? null : "'"+boolValue+"'";
    }
    private String convertToSQL(Mood mood) {
        return (mood == null) ? null : "'"+mood.name()+"'";
    }

}
