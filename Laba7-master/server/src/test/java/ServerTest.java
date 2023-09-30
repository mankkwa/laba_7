import org.junit.*;
import org.junit.jupiter.api.Test;
import ru.itmo.common.model.Car;
import ru.itmo.common.model.Coordinates;
import ru.itmo.common.model.HumanBeing;
import ru.itmo.common.model.Mood;
import ru.itmo.server.JDBC.JdbcManager;

import java.sql.Connection;
import java.sql.SQLException;
import ru.itmo.server.collection.commands.*;

/**
 * Класс для серверных тестов
 */
public class ServerTest {
    private static Connection connection;
    AddCommand addCommand = new AddCommand();

    private HumanBeing returnHuman() {
        Coordinates coord = new Coordinates(5, 5F);
        Car car = new Car("name", false);
        return new HumanBeing("name", "soundtrackName", 5L, 5,
        false, false, coord, Mood.RAGE, car);
    }

    @Before
    public void init() throws SQLException {
        connection = JdbcManager.connectToDataBase();
    }
    @After
    public void close() throws SQLException {
        connection.close();
    }

    @Test
    public void addTest() {
        addCommand.execute(returnHuman());
    }
}

