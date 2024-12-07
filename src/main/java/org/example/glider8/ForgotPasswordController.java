package org.example.glider8;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;


public class ForgotPasswordController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField securityQuestionField;

    @FXML
    private TextField securityAnswerField;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button backToLoginButton;

    public void initialize() {

    }
    @FXML
    private void forgotPasswordButtonClick() {
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loaderBackLogin = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
            Parent mainMenuPage = loaderBackLogin.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainMenuPage));
            stage.setTitle("Main Menu");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
