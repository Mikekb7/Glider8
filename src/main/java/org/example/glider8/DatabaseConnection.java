package org.example.glider8;

import java.sql.*;

public class DatabaseConnection {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://gliderserver.mysql.database.azure.com:3306/gliderdatabase?useSSL=true&serverTimezone=UTC";
    private static final String DB_USERNAME = "glider";
    private static final String DB_PASSWORD = "Gpassword123";



    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connection established successfully.");
            return connection;
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }


    public static void main(String[] args) {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                // CRUD
                PreparedStatement ps = connection.prepareStatement(Queries.login);
                ps.setString(1, "Mike123");
                ps.setString(2, "Password123");

                // Run the query
                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()) {
                    System.out.println(resultSet.getString("Username") + resultSet.getString("Password"));
                }

            } catch (SQLException e) {
                System.out.println("Connection failed: " + e.getMessage());
            }
        }
    }
}