package org.example.glider8;

// Import necessary JavaFX and SQL classes
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.glider8.common.Actions;



import java.sql.*;
import java.util.Objects;


public class HelloController {
    // FXML elements defined in the corresponding FXML file

    @FXML
private TextField customerUsernameField;
@FXML
private TextField adminUsernameField;
@FXML
private PasswordField customerPasswordField;
@FXML
private PasswordField adminPasswordField;
@FXML
private Button adminLoginButton;
@FXML
private Button customerLoginButton;
@FXML
private Button forgotPasswordButton;
@FXML
private Button signUpButton;
@FXML
private Label adminLoginStatusLabel;
@FXML
private Label customerLoginStatusLabel;
@FXML
private Button backToLoginButton;

// Label to display the login status



    private Connection connection; // Database connection object

@FXML
    private void initialize() {
    // Establish a database connection
        connection = DatabaseConnection.getConnection();
    // Set actions for buttons
        customerLoginButton.setOnAction(e -> {
            try {
                loginButtonClick(e); // Call customer login logic
            } catch (Exception ex) {
                // Display error message if login fails
                customerLoginStatusLabel.setText("Error occurred while logging user to the system. Please try again." + ex.getMessage());
                ex.printStackTrace();
            }
        });
    adminLoginButton.setOnAction(e -> {
        try {
            adminLoginButtonClick(e); // Call admin login logic
        } catch (Exception ex) {
            // Display error message if admin login fails
            adminLoginStatusLabel.setText("Error occurred while logging admin to the system. Please try again." + ex.getMessage());
            ex.printStackTrace();
        }
    });
    // Set actions for forgot password and sign-up buttons

        forgotPasswordButton.setOnAction(this::forgotPasswordButtonClick);
        signUpButton.setOnAction(this::signUpButtonClick);
    }
    // Ensures the database connection is established
    private void connectToDatabase() {
        String url = "jdbc:mysql://gliderserver.mysql.database.azure.com:3306/gliderdatabase?useSSL=true&serverTimezone=UTC";
        String username = "glider"; // Replace with actual username
        String password = "Gpassword123"; // Replace with actual password

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            // Display error message if database connection fails
            System.err.println("Error connecting to database: " + e.getMessage());
            customerLoginStatusLabel.setText("Failed to connect to database.");
        }
    }
    // Handles customer login logic
      @FXML
      private void loginButtonClick(ActionEvent event) throws SQLException {
          String customerUsernameInput = customerUsernameField.getText().trim();
          String customerPasswordInput = customerPasswordField.getText().trim();

          // Validate input fields
          if (customerPasswordInput.isEmpty() || customerPasswordInput.isEmpty()) {
              customerLoginStatusLabel.setText("Please enter both username and password.");
              return;
          }

          // Ensure database connection is established
          if (connection == null) {
              customerLoginStatusLabel.setText("Database connection not available.");
              return;
          }
          //Query to check if user exists
          String loginQuery = "SELECT * FROM user where username = ? and password = ?";

          try (PreparedStatement preparedStatement = connection.prepareStatement(loginQuery)) {
              // Set the username and password parameters
              preparedStatement.setString(1, customerUsernameInput);
              preparedStatement.setString(2, customerPasswordInput);

              try (ResultSet resultSet = preparedStatement.executeQuery()){
                  if (resultSet.next()){
                      // Display success message if login is successful
                      Session.setCredentials(customerUsernameInput, customerPasswordInput);

                      customerLoginStatusLabel.setText("Login is successful. Welcome, " + resultSet.getString("username") + " :) ");
                      // Navigate to Reservations.fxml after a delay


                      PauseTransition pause = new PauseTransition(Duration.seconds(1));
                      pause.setOnFinished(e -> {
                          try {
                              FXMLLoader reservationsLoader = new FXMLLoader(getClass().getResource("Reservations.fxml"));
                              Parent nextScene = reservationsLoader.load();
                              Scene reservationsScene = new Scene(nextScene);
                              // Set the scene to the primary stage and update its title
                              Stage reserveStage = (Stage) customerLoginStatusLabel.getScene().getWindow();
                              reserveStage.setScene(reservationsScene);
                              reserveStage.setTitle("Reservations");
                          } catch (Exception ex){
                              // Display error message if navigation fails
                              ex.printStackTrace();
                          }
                      });
                      pause.play();
                  } else {
                      // Display error message if login fails
                      customerLoginStatusLabel.setText("Login is unsuccessful. Please try again, " + resultSet.getString("username") + " :( ");
                  }
              }
          } catch (SQLException ex){
              // Display error message if user does not exist
              customerLoginStatusLabel.setText("User does not exsit, please register.");
              ex.printStackTrace();

          }
      }
      // Handles admin login logic
    @FXML
    private void adminLoginButtonClick(ActionEvent event) throws SQLException {
        String adminUsernameInput = adminUsernameField.getText().trim();
        String adminPasswordInput = adminPasswordField.getText().trim();

        // Validate input fields
        if (adminUsernameInput.isEmpty() || adminPasswordInput.isEmpty()) {
            customerLoginStatusLabel.setText("Please enter both username and password.");
            return;
        }

        // Ensure database connection is established
        if (connection == null) {
            customerLoginStatusLabel.setText("Database connection not available.");
            return;
        }
        //Query to check if user exists
        String loginQuery = "SELECT * FROM user where username = ? and password = ? and role = 'admin'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(loginQuery)) {
            // Set the username and password parameters
            preparedStatement.setString(1, adminUsernameInput);
            preparedStatement.setString(2, adminPasswordInput);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    adminLoginStatusLabel.setText("Login is successful. Welcome, " + resultSet.getString("username") + " :) ");
                    // Navigate to AdminPage.fxml after a delay
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> {
                        try {
                            // Load the AdminPage.fxml file after the splash screen delay
                            FXMLLoader reservationsLoader = new FXMLLoader(getClass().getResource("AdminPage.fxml"));
                            Parent nextScene = reservationsLoader.load();
                            Scene reservationsScene = new Scene(nextScene);
                            Stage reserveStage = (Stage) adminLoginStatusLabel.getScene().getWindow();
                            reserveStage.setScene(reservationsScene);
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                    });
                    // Start the delay for the splash screen transition
                    pause.play();
                } else {
                    adminLoginStatusLabel.setText("Login is unsuccessful. Please try again, " + resultSet.getString("username") + " :( ");
                }
            }
        } catch (SQLException ex){
            // Display error message if user does not exist
            adminLoginStatusLabel.setText("This account does not have admin access.");
            ex.printStackTrace();

        }
    }
    // Handles forgot password button click
    @FXML
    private void forgotPasswordButtonClick(ActionEvent event) {
        // Load the ForgotPassword.fxml file
        Actions.loadFXML(event, "/org/example/glider8/ForgotPassword.fxml", "Forgot Password");
    }
    // Handles sign-up button click
    private void signUpButtonClick(ActionEvent event) {
        Actions.loadFXML(event, "/org/example/glider8/Register-view.fxml", "Sign Up");
    }

    // Handles logout functionality
    @FXML
    private void logoutButtonClick(ActionEvent event) {
        System.out.println("Logout button clicked."); // Debug statement
        try {
            System.out.println(getClass().getResource("/org/example/glider8/MainMenu.fxml")); // Debug file path
            Parent mainMenuRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot));
            stage.setTitle("Main Menu");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading MainMenu.fxml: " + e.getMessage());
        }

    }

    // Getter and Setter methods for buttons
@FXML
    private Button getSignUpButton() {
        return signUpButton;
    }

    private void setSignUpButton(Button signUpButton) {
        this.signUpButton = signUpButton;
    }

    private Button getForgotPasswordButton() {
        return forgotPasswordButton;
    }

    private void setForgotPasswordButton(Button forgotPasswordButton) {
        this.forgotPasswordButton = forgotPasswordButton;
    }
}