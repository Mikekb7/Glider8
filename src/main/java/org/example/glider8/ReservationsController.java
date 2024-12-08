package org.example.glider8;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

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
    @FXML
    private TableColumn<Flight, String> availableSeats;
    @FXML
    private TableColumn<Flight, String> capacity;

    @FXML
    private Button bookFlightButton; // Link to "Book a New Flight" button

    @FXML
    private Button LogoutButton; // Logout button

    private Connection connection;

    public void initialize() {
        connectToDatabase();

        // Bind the TableView columns to the Flight model class
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        departureCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        departureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
        destinationTime.setCellValueFactory(new PropertyValueFactory<>("destinationTime"));
        airline.setCellValueFactory(new PropertyValueFactory<>("airline"));
        availableSeats.setCellValueFactory(new PropertyValueFactory<>("seatsAvailable"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        // Set the default placeholder for the TableView
        bookedFlightsTable.setPlaceholder(new Label("You have no booked flights."));
        LogoutButton.setOnAction(this::logoutButtonClick);
    }

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

    @FXML
    private void handleEnterAction() {
        String reservationId = reservationIdField.getText().trim();

        // Validate the input
        if (reservationId.isEmpty()) {
            bookedFlightsTable.setPlaceholder(new Label("Please enter a reservation ID."));
            bookedFlightsTable.getItems().clear();
            return;
        }

        // Clear previous data from the TableView
        bookedFlightsTable.getItems().clear();

        // Query the database to find flight information for the reservation
        String query = """
            SELECT 
                flights.flight_number,
                flights.departure_city,
                flights.departure_time,
                flights.destination_city,
                flights.destination_time,
                flights.airline,
                flights.available_seats,
                flights.capacity
            FROM 
                reservations
            JOIN 
                flights 
            ON 
                reservations.flight_number = flights.flight_number
            WHERE 
                reservations.reservation_id = ?;
            """;

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
                            resultSet.getString("airline"),
                            resultSet.getString("available_seats"),
                            resultSet.getString("capacity")
                    ));
                }

                if (flights.isEmpty()) {
                    // If no flights are found, update the placeholder
                    bookedFlightsTable.setPlaceholder(new Label("Invalid reservation ID. No flights found."));
                } else {
                    // Populate the TableView with flight data
                    bookedFlightsTable.setItems(flights);
                }
            }
        } catch (SQLException e) {
            bookedFlightsTable.setPlaceholder(new Label("An error occurred while fetching the reservation."));
            e.printStackTrace();
        }
    }


    @FXML
    protected void bookFlightButtonClick(ActionEvent event) {
        System.out.println("Logout button clicked."); // Debug statement
        try {
            System.out.println(getClass().getResource("/org/example/glider8/Booking.fxml")); // Debug file path
            Parent mainMenuRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Booking.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot));
            stage.setTitle("Book a Flight");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Booking.fxml: " + e.getMessage());
        }
    }


    @FXML
    protected void logoutButtonClick(ActionEvent event) {
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
@FXML
    protected void loginButtonClick( ){
    System.out.println("Error loading MainMenu.fxml: ");

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
