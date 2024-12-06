package org.example.glider8;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;


public class HelloController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button forgotPasswordButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label loginStatusLabel;
// Label to display the login status

    private Connection connection;

    public void initialize() {
        connectToDatabase();
        loginButton.setOnAction(e -> {
            try {
                loginButtonClick(e);
            } catch (SQLException ex) {
                loginStatusLabel.setText("Error occurred while logging user to the system. Please try again." + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
    private void connectToDatabase() {
        String url = "jdbc:mysql://gliderserver.mysql.database.azure.com:3306/gliderdatabase?useSSL=true&serverTimezone=UTC";
        String username = "glider"; // Replace with actual username
        String password = "Gpassword123"; // Replace with actual password

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            loginStatusLabel.setText("Failed to connect to database.");
        }
    }


    protected void loginButtonClick(ActionEvent event) throws SQLException {
        String usernameInput = usernameField.getText().trim();
        String passwordInput = passwordField.getText().trim();

        // Validate input fields
        if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
            loginStatusLabel.setText("Please enter both username and password.");
            return;
        }

        // Ensure database connection is established
        if (connection == null) {
            loginStatusLabel.setText("Database connection not available.");
            return;
        }

       // loginButton.setText(("Executing log in: " + usernameInput + passwordInput));

        String loginQuery = "SELECT * FROM user where username = ? and password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(loginQuery)) {
            preparedStatement.setString(1, usernameInput);
            preparedStatement.setString(2, passwordInput);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    loginStatusLabel.setText("Login is successful. Welcome, " + resultSet.getString("username") + " :) ");
                } else {
                    loginStatusLabel.setText("Login is unsuccessful. Please try again, " + resultSet.getString("username") + " :( ");
                }
            }
        } catch (SQLException ex){
            loginStatusLabel.setText("Error executing login query: " + ex.getMessage());
            ex.printStackTrace();

        }
    }

}