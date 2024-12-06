package org.example.glider8;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.animation.PauseTransition;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("Splash Screen.fxml"));

        Parent splashRoot = splashLoader.load();
        Scene splashScene = new Scene(splashRoot, 600, 400);

        stage.setScene(splashScene);

        stage.setTitle("Welcome to Glider!");
        stage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            try {
                // Load the Main Menu FXML after the splash
                FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                Parent mainMenuRoot = mainMenuLoader.load();
                Scene mainMenuScene = new Scene(mainMenuRoot);

                stage.setScene(mainMenuScene);
                stage.setTitle("Main Menu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}