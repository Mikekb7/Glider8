package org.example.glider8;

import java.sql.*;

public class DatabaseConnection {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://gliderserver.mysql.database.azure.com:3306/gliderdatabase?useSSL=true&serverTimezone=UTC";
    private static final String DB_USERNAME = "glider";
    private static final String DB_PASSWORD = "Gpassword123";



    public static Connection getConnection() {
        // Establish a connection to the database
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connection established successfully.");
            return connection;
            // Catch any exceptions that occur
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }


    public static void main(String[] args) {
        // Get a connection to the database
        Connection connection = getConnection();
        if (connection != null) {
            try {
                // CRUD
                PreparedStatement ps = connection.prepareStatement(Queries.login);
                ps.setString(1, "Mike123"); // Set the first parameter in the query to "Mike123"
                ps.setString(2, "Password123"); // Set the second parameter in the query to "Password123"

                // Run the query
                ResultSet resultSet = ps.executeQuery(); // Execute the query and store the results in a ResultSet

                while (resultSet.next()) {
                    System.out.println(resultSet.getString("Username") + resultSet.getString("Password"));
                }

            } catch (SQLException e) {
                System.out.println("Connection failed: " + e.getMessage());
            }
        }
    }
}