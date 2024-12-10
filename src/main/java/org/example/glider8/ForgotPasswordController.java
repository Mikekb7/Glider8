package org.example.glider8;
// Import necessary JavaFX and SQL classes
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.glider8.common.Actions;
import java.sql.*;


import java.io.IOException;

import static org.example.glider8.DatabaseConnection.getConnection;


public class ForgotPasswordController {
    // FXML elements defined in the corresponding FXML file
    @FXML
    private TextField usernameField;

    @FXML
    private TextField securityAnswerField;

    @FXML
    private Label securityQuestionLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button backToLoginButton;

    @FXML
    private Button handleRetrievePassword;

    private Connection connection;


// Initializes the controller and sets up button actions
    public void initialize() {
        connectToDatabase();
        passwordLabel.setText("");
        backToLoginButton.setOnAction(this::backToLoginButtonClick);
        handleRetrievePassword.setOnAction(this::handleRetrievePasswordClick);

    }
    // Connect to the database
    private void connectToDatabase() {
        String url = "jdbc:mysql://gliderserver.mysql.database.azure.com:3306/gliderdatabase?useSSL=true&serverTimezone=UTC";
        String username = "glider"; // Replace with your database username
        String password = "Gpassword123"; // Replace with your database password

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully.");
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }
// Handle the back to login button click
    @FXML
    private void backToLoginButtonClick(ActionEvent event) {
        Actions.loadFXML(event, "/org/example/glider8/MainMenu.fxml", "Main Menu"); // Load the Main Menu FXML using the Actions class
    }

// Handle the retrieve password button click
    @FXML
    private void handleRetrievePasswordClick(ActionEvent event) { // Handle the retrieve password button click event
        String username = usernameField.getText().trim();
        String securityAnswer = securityAnswerField.getText().trim();
// Check if the username and security answer fields are empty
        if (username.isEmpty() || securityAnswer.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please enter both username and security answer.");
            return;
        }// Retrieve the password from the database
// Query to retrieve the password based on the username and security answer
        String query = "SELECT password FROM user WHERE username = ? AND securityanswer = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {// Prepare the statement with the query
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, securityAnswer);
            ResultSet resultSet = preparedStatement.executeQuery();
// Check if the result set has any rows
            if (resultSet.next()) {
                // Retrieve the password from the database
                String password = resultSet.getString("password");
                // Display the password in the label
                passwordLabel.setText("Your Password: " + password); // Set the password label text with the retrieved password
            } else {
                // Handle case where username and security answer do not match
                showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "The username or security answer is incorrect.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while retrieving the password.");
        }
    }

// Show an alert with the specified type, title, and message
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}