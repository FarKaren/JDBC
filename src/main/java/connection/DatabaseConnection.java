package connection;

import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static Connection connection;
    private static final String url = "jdbc:postgresql://localhost:5432/test_db";
    private static final String username = "user";
    private static final String password = "user";
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
