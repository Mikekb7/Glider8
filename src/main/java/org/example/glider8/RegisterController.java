package org.example.glider8;
// Import necessary JavaFX and SQL classes

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.glider8.common.Actions;

import java.sql.*;

import static org.example.glider8.Queries.insertQuery;

public class RegisterController {
    // FXML elements defined in the corresponding FXML file

    @FXML
    private Button backToLoginButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField stateField;

    @FXML
    private TextField zipCodeField;

    @FXML
    private TextField ssnField;

    @FXML
    private Label securityQuestionField;

    @FXML
    private TextField securityAnswerField;

    @FXML
    private Button signUpButton;

    private Connection connection;

    // Initializes the controller and sets up button actions
    @FXML
    private void initialize() {
        connectToDatabase();
        // Called automatically when the FXML is loaded
        System.out.println("Sign Up Page Loaded Successfully!");
        signUpButton.setOnAction(this::handleSignUpAction);
        backToLoginButton.setOnAction(this::backToLoginButtonClick);
    }
    // Connects to the database using the JDBC driver
    private void connectToDatabase() {
        String url = "jdbc:mysql://gliderserver.mysql.database.azure.com:3306/gliderdatabase?useSSL=true&serverTimezone=UTC";
        String username = "glider"; // Replace with your database username
        String password = "Gpassword123"; // Replace with your database password

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to the database.");
        }
    }
    // Handles the sign up button action
    @FXML
    private void handleSignUpAction(ActionEvent event) { // Method to handle sign up button click to check if method listens to button click
        System.out.println("Sign Up button clicked");
        // Validate the user input before proceeding
        if (!validateInputs()) {
            return;
        }
// Get the user input from the text fields
        String username = usernameField.getText().trim(); // Get the text from the username field and trim any leading or trailing whitespace
        String password = passwordField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String state = stateField.getText().trim();
        String zipCode = zipCodeField.getText().trim();
        String securityAnswer = securityAnswerField.getText().trim();
        String ssn = ssnField.getText().trim();


        // Insert the user data into the database
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            // Populate the SQL query with user data
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, address);
            preparedStatement.setString(7, city);
            preparedStatement.setString(8, state);
            preparedStatement.setString(9, zipCode);
            preparedStatement.setString(10, securityAnswer);
            preparedStatement.setString(11, ssn);


            int rowsAffected = preparedStatement.executeUpdate(); // Execute the query and get the number of rows affected

            if (rowsAffected > 0) { // Check if the query was successful
                showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully!");
                System.out.println("User inserted into the database.");
            } else {
                // Show an error message if the query failed
                showAlert(Alert.AlertType.ERROR, "Failure", "User registration failed. Please try again.");
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while registering the user. Please try again.");
        }
    }
// Method to handle back to login button click when clicked event is triggered
    public void backToLoginButtonClick(ActionEvent event) {
        Actions.loadFXML(event, "/org/example/glider8/MainMenu.fxml", "Main Menu");//uses the Actions class to load the MainMenu.fxml file
    }


// Method to validate user input before proceeding with the sign-up process
    private boolean validateInputs() {
        if (usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty() || // Check if any of the fields are empty and show a warning
                firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() || addressField.getText().trim().isEmpty() ||
                cityField.getText().trim().isEmpty() || stateField.getText().trim().isEmpty() ||
                zipCodeField.getText().trim().isEmpty() || ssnField.getText().trim().isEmpty() || securityAnswerField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill in all fields before signing up.");
            return false; // Return false if any fields are empty
        }

        if (!emailField.getText().contains("@")) { // Check if the email field contains the "@" symbol
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return false;
        }
// Check if the SSN field contains exactly 9 digits
        if (!ssnField.getText().matches("\\d{9}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid SSN", "SSN must be 9 digits long.");
            return false; // Return false if the SSN is invalid
        }

        return true;
    }

// Method to show an alert dialog with a specific type, title, and message
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);//sets the title of the alert dialog
        alert.setHeaderText(null);
        alert.setContentText(message);//sets the message of the alert dialog
        alert.showAndWait();
    }
}
