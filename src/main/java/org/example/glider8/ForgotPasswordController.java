/*package org.example.glider8;

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



    public void initialize() {
        getConnection();
        passwordLabel.setText("");
    }


    @FXML
    private void forgotPasswordButtonClick() {
    }

    @FXML
    private void backToLoginClick(ActionEvent event) {
        Actions.loadFXML(event, "/org/example/glider8/MainMenu.fxml", "Sign Up");
    }
    }

    @FXML
    private void handleRetrievePassword(ActionEvent event) {
        String username = usernameField.getText().trim();
        String securityAnswer = securityAnswerField.getText().trim();

        if (username.isEmpty() || securityAnswer.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please enter both username and security answer.");
            return;
        }

        String query = "SELECT password FROM user WHERE username = ? and security_answer = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String correctAnswer = resultSet.getString("security_answer");
                String password = resultSet.getString("password");


                if (securityAnswer.equalsIgnoreCase(correctAnswer)) {
                    passwordLabel.setText("Your Password: " + password);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Incorrect Answer", "The security answer is incorrect.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "User Not Found", "No user found with the entered username.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while retrieving the password.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}*/