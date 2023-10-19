package galactic_messenger.server;
import java.sql.*;


public class DatabaseInitializer {

    public static void initializeDatabase(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            // Créez vos tables et effectuez d'autres opérations d'initialisation ici
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(255), password VARCHAR(255))");
            statement.executeQuery("insert into users (name, password) values ('Will Smith','password')");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}