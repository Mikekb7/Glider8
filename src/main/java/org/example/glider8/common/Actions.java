package org.example.glider8.common;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Actions {
    public static void loadFXML(ActionEvent event, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Actions.class.getResource(fxmlFile)));
            Parent page = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(page));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + fxmlFile);
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("FXML file not found: " + fxmlFile);
            e.printStackTrace();
        }
    }

    public static void handleLogin(
            Connection connection,
            TextField usernameField,
            PasswordField passwordField,
            Label statusLabel,
            String reservationsFXML
    ) {
        String customerUsernameInput = usernameField.getText().trim();
        String customerPasswordInput = passwordField.getText().trim();

        // Validate input fields
        if (customerUsernameInput.isEmpty() || customerPasswordInput.isEmpty()) {
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        // Ensure database connection is established
        if (connection == null) {
            statusLabel.setText("Database connection not available.");
            return;
        }

        String loginQuery = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(loginQuery)) {
            preparedStatement.setString(1, customerUsernameInput);
            preparedStatement.setString(2, customerPasswordInput);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    statusLabel.setText("Login is successful. Welcome, " + resultSet.getString("username") + " :)");

                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> {
                        try {
                            loadFXML(new ActionEvent(), reservationsFXML, "Reservations");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    pause.play();
                } else {
                    statusLabel.setText("Login is unsuccessful. Please try again.");
                }
            }
        } catch (SQLException ex) {
            statusLabel.setText("Error executing login query: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
