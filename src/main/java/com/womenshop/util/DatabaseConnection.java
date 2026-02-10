package com.womenshop.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/womenshop";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found", e);
            }
        }
        return connection;
    }

    public static void initializeDatabase() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        String createClothingTable =
                "CREATE TABLE IF NOT EXISTS clothing (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "price_buy DOUBLE NOT NULL CHECK (price_buy >= 0), " +
                        "price_sell DOUBLE NOT NULL CHECK (price_sell >= 0), " +
                        "discount_price DOUBLE DEFAULT 0 CHECK (discount_price >= 0), " +
                        "stock INT DEFAULT 0 CHECK (stock >= 0), " +
                        "size INT NOT NULL CHECK (size >= 34 AND size <= 54))";

        String createShoesTable =
                "CREATE TABLE IF NOT EXISTS shoes (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "price_buy DOUBLE NOT NULL CHECK (price_buy >= 0), " +
                        "price_sell DOUBLE NOT NULL CHECK (price_sell >= 0), " +
                        "discount_price DOUBLE DEFAULT 0 CHECK (discount_price >= 0), " +
                        "stock INT DEFAULT 0 CHECK (stock >= 0), " +
                        "size INT NOT NULL CHECK (size >= 36 AND size <= 50))";

        String createAccessoryTable =
                "CREATE TABLE IF NOT EXISTS accessory (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "price_buy DOUBLE NOT NULL CHECK (price_buy >= 0), " +
                        "price_sell DOUBLE NOT NULL CHECK (price_sell >= 0), " +
                        "discount_price DOUBLE DEFAULT 0 CHECK (discount_price >= 0), " +
                        "stock INT DEFAULT 0 CHECK (stock >= 0), " +
                        "size INT NULL)";

        stmt.execute(createClothingTable);
        stmt.execute(createShoesTable);
        stmt.execute(createAccessoryTable);
        stmt.close();
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}