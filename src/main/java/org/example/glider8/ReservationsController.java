package org.example.glider8;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReservationsController {

    @FXML
    private TextField reservationIdField; // Field to input reservation ID
    @FXML
    private TableView<Flight> bookedFlightsTable; // TableView to display flights
    @FXML
    private TableColumn<Flight, String> flightNumberColumn;
    @FXML
    private TableColumn<Flight, String> departureCityColumn;
    @FXML
    private TableColumn<Flight, String> departureTime;
    @FXML
    private TableColumn<Flight, String> destinationColumn;
    @FXML
    private TableColumn<Flight, String> destinationTime;
    @FXML
    private TableColumn<Flight, String> airline;

    private Connection connection;

    // Method to initialize database connection and TableView columns
    public void initialize() {
        connectToDatabase();

        // Bind the TableView columns to the Flight model class
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        departureCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        departureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
        destinationTime.setCellValueFactory(new PropertyValueFactory<>("destinationTime"));
        airline.setCellValueFactory(new PropertyValueFactory<>("airline"));
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

    // Handle the "Enter" button click
    @FXML
    private void handleEnterAction() {
        String reservationId = reservationIdField.getText().trim();

        // Validate the input
        if (reservationId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a reservation ID.");
            return;
        }

        // Clear previous data from the TableView
        bookedFlightsTable.getItems().clear();

        // Query the database to find the reservation
        String query = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, reservationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ObservableList<Flight> flights = FXCollections.observableArrayList();

                // Add flight data to the list
                while (resultSet.next()) {
                    flights.add(new Flight(
                            resultSet.getString("flight_number"),
                            resultSet.getString("departure_city"),
                            resultSet.getString("departure_time"),
                            resultSet.getString("destination_city"),
                            resultSet.getString("destination_time"),
                            resultSet.getString("airline")
                    ));
                }

                if (flights.isEmpty()) {
                    // If no flights are found
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "Couldn't find a flight with the provided reservation ID. Please try again.");
                } else {
                    // Populate the TableView with flight data
                    bookedFlightsTable.setItems(flights);
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while fetching the reservation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}













/*import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReservationsController {

   /* @FXML
    private Button mainMenuButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField reservationIdField;

 /*     @FXML
        private TableView<Flights> bookedFlightsTable;

        @FXML
        private TableColumn<Flights, String> flightNumberColumn;

        @FXML
        private TableColumn<Flights, String> departureCityColumn;

        @FXML
        private TableColumn<Flights, String> departureTimeColumn;

        @FXML
        private TableColumn<Flights, String> destinationColumn;

        @FXML
        private TableColumn<Flights, String> destinationTimeColumn;

        @FXML
        private TableColumn<Flights, String> airlineColumn;

        @FXML
        private Button bookFlightButton;

        private final ObservableList<Flights> flightData = FXCollections.observableArrayList();
*/
    /*public void initialize() {

    }
}
            // Load the flight data into the table
 /*           loadAvailableFlights();
        }

       private void loadAvailableFlights() {
            flightData.clear(); // Clear any previous data

            String url = "jdbc:mysql://localhost:3306/your_database_name";
            String user = "your_database_user";
            String password = "your_database_password";

            String query = "SELECT flight_number, departure_city, departure_time, destination_city, destination_time, airline FROM flights WHERE available_seats > 0";

            try (Connection connection = DriverManager.getConnection(url, user, password);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Flights flight = new Flights(
                            resultSet.getString("flight_number"),
                            resultSet.getString("departure_city"),
                            resultSet.getString("departure_time"),
                            resultSet.getString("destination_city"),
                            resultSet.getString("destination_time"),
                            resultSet.getString("airline")
                    );
                    flightData.add(flight); // Add each flight to the observable list
                }

                bookedFlightsTable.setItems(flightData); // Bind data to the table

            } catch (Exception e) {
                showAlert("Error", "Unable to load flights: " + e.getMessage());
                e.printStackTrace();
            }
        }

        @FXML
        private void handleBookFlightButtonClick() {
            try {
                // Load the new FXML for the booking page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("BookFlightForm.fxml"));
                Parent bookingPage = loader.load();

                // Set up a new scene and stage for the booking page
                Stage stage = (Stage) bookFlightButton.getScene().getWindow(); // Get current stage
                stage.setScene(new Scene(bookingPage));
                stage.setTitle("Book a Flight");
                stage.show();

            } catch (Exception e) {
                showAlert("Error", "Unable to navigate to the booking page: " + e.getMessage());
                e.printStackTrace();
            }
        }

        @FXML
        private void handleMainMenuButtonClick() {
            try {
                // Load the main menu FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                Parent mainMenuPage = loader.load();

                // Set up a new scene and stage for the main menu
                Stage stage = (Stage) mainMenuButton.getScene().getWindow(); // Get current stage
                stage.setScene(new Scene(mainMenuPage));
                stage.setTitle("Main Menu");
                stage.show();

            } catch (Exception e) {
                showAlert("Error", "Unable to navigate to the main menu: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
}*/
