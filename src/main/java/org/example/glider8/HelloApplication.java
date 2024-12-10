package org.example.glider8;

// Import necessary JavaFX and utility classes

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.animation.PauseTransition;
import org.example.glider8.common.Actions;

import java.io.IOException;
// Main application class that extends JavaFX Application
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the Splash Screen FXML file
        FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("Splash Screen.fxml"));
        // Set the root node for the splash scene
        Parent splashRoot = splashLoader.load();
        Scene splashScene = new Scene(splashRoot, 800, 600); // Create a scene with specific dimensions
        // Set the scene to the primary stage (window)
        stage.setScene(splashScene);

        // Set the title of the application window

        stage.setTitle("Welcome to Glider!");
        stage.show();

        // Create a PauseTransition to delay the splash screen for 5 seconds

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        // Specify what happens after the delay
        delay.setOnFinished(event -> {
            try {
                //Actions.loadFXML(new ActionEvent(), "/org/example/glider8/MainMenu.fxml", "Main Menu");

                // Load the Main Menu FXML file after the splash screen delay
                FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                Parent mainMenuRoot = mainMenuLoader.load(); // Root node for the main menu
                // Create and set a new scene for the Main Menu

                Scene mainMenuScene = new Scene(mainMenuRoot);

                // Set the Main Menu scene to the primary stage and update its title
                stage.setScene(mainMenuScene);
                stage.setTitle("Main Menu");
            } catch (Exception e) {
                // Handle any exceptions that occur while loading the Main Menu FXML
                e.printStackTrace();
            }
        });
        // Start the delay for the splash screen transition

        delay.play();
    }
    // Main method t launch the JavaFX application
// Launches the application and calls the `start` method
    public static void main(String[] args) {
        launch(args);
    }
}