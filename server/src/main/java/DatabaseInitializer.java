import java.sql.*;


public class DatabaseInitializer {

    public static void initializeDatabase(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            // Créez vos tables et effectuez d'autres opérations d'initialisation ici
            statement.execute("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), password VARCHAR(255))");
            statement.execute("insert into users (name, password) values ('Will Smith','password')");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}