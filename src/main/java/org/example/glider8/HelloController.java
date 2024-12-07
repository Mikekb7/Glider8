package org.example.glider8;


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



    private Connection connection;

@FXML
    private void initialize() {
        connection = DatabaseConnection.getConnection();
        customerLoginButton.setOnAction(e -> {
            try {
                loginButtonClick(e);
            } catch (Exception ex) {
                customerLoginStatusLabel.setText("Error occurred while logging user to the system. Please try again." + ex.getMessage());
                ex.printStackTrace();
            }
        });
/*
    private void initialize() {
        customerLoginButton.setOnAction(e -> {
            try {
                adminLoginButton(e);
            } catch (SQLException ex) {
                adminLoginStatusLabel.setText("Error occurred while logging user to the system. Please try again." + ex.getMessage());
                ex.printStackTrace();
            }
        });*/

        forgotPasswordButton.setOnAction(this::forgotPasswordButtonClick);
        signUpButton.setOnAction(this::signUpButtonClick);

        //backToLoginButton.setOnAction(this::forgotPasswordButtonClick);
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
            customerLoginStatusLabel.setText("Failed to connect to database.");
        }
    }
    /*
      @FXML
      private void loginButtonClick(ActionEvent event) {
          Actions.handleLogin(
                  connection,
                  customerUsernameField,
                  customerPasswordField,
                  customerLoginStatusLabel,
                  "Reservations.fxml"
          );
      }*/

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

         // loginButton.setText(("Executing log in: " + usernameInput + passwordInput));

          String loginQuery = "SELECT * FROM user where username = ? and password = ?";

          try (PreparedStatement preparedStatement = connection.prepareStatement(loginQuery)) {
              preparedStatement.setString(1, customerUsernameInput);
              preparedStatement.setString(2, customerPasswordInput);

              try (ResultSet resultSet = preparedStatement.executeQuery()){
                  if (resultSet.next()){
                      customerLoginStatusLabel.setText("Login is successful. Welcome, " + resultSet.getString("username") + " :) ");

                      PauseTransition pause = new PauseTransition(Duration.seconds(1));
                      pause.setOnFinished(e -> {
                          try {
                              FXMLLoader reservationsLoader = new FXMLLoader(getClass().getResource("Reservations.fxml"));
                              Parent nextScene = reservationsLoader.load();
                              Scene reservationsScene = new Scene(nextScene);
                              Stage reserveStage = (Stage) customerLoginStatusLabel.getScene().getWindow();
                              reserveStage.setScene(reservationsScene);
                          } catch (Exception ex){
                              ex.printStackTrace();
                          }
                      });
                      pause.play();
                  } else {
                      customerLoginStatusLabel.setText("Login is unsuccessful. Please try again, " + resultSet.getString("username") + " :( ");
                  }
              }
          } catch (SQLException ex){
              customerLoginStatusLabel.setText("Error executing login query: " + ex.getMessage());
              ex.printStackTrace();

          }
      }
    @FXML
    private void forgotPasswordButtonClick(ActionEvent event) {
        /*try {
            System.out.println("Debug: Checking path for ForgotPassword.fxml...");//dont erase this
            System.out.println(getClass().getResource("ForgotPassword.fxml")); //dont erase this

            //FXMLLoader forgotPasswordLoader = new FXMLLoader(getClass().getResource("ForgotPassword.fxml"));
            FXMLLoader forgotPasswordLoader = new FXMLLoader();

            Parent forgotPasswordPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ForgotPassword.fxml")));
            Stage forgotPasswordStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            forgotPasswordStage.setScene(new Scene(forgotPasswordPage));
            forgotPasswordStage.setTitle("Forgot Password");
            forgotPasswordStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
        Actions.loadFXML(event, "/org/example/glider8/ForgotPassword.fxml", "Forgot Password");
    }
    private void signUpButtonClick(ActionEvent event) {
        Actions.loadFXML(event, "/org/example/glider8/ForgetPassword.fxml", "Sign Up");
    }
    /*private void loginButtonClick(ActionEvent event) {
        Actions.loadFXML(event, "/org/example/glider8/Reservations.fxml", "Forgot Password");
    }
    private void backToLoginClick(ActionEvent event) {
        Actions.loadFXML(event, "/org/example/glider8/MainMenu.fxml", "Sign Up");
    }*/



    //backToLoginClick
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