package org.example.glider8;

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
    private void backToLoginClick(ActionEvent event) {
        Actions.loadFXML(event, "/org/example/glider8/MainMenu.fxml", "Sign Up");
    }
}
