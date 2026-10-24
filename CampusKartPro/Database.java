package store;

import java.sql.*;

public class Database {
    // Change these values to match your local MySQL installation.
    private static final String URL = "jdbc:mysql://localhost:3306/campuskart";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

