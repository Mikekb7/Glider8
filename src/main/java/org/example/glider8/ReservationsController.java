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

import java.sql.*;
import java.util.Objects;

public class ReservationsController {

    @FXML
    private TextField reservationIdField; // Field to input reservation ID
    @FXML
    private TableView<Reservations> bookedFlightsTable; // TableView to display flights
    @FXML
    private TableColumn<Reservations, String> flightNumberColumn;
    @FXML
    private TableColumn<Reservations, String> departureCityColumn;
    @FXML
    private TableColumn<Reservations, String> departureTime;
    @FXML
    private TableColumn<Reservations, String> destinationColumn;
    @FXML
    private TableColumn<Reservations, String> destinationTime;
    @FXML
    private TableColumn<Reservations, String> airline;
    @FXML
    private TableColumn<Reservations, Integer> availableSeats;
    @FXML
    private TableColumn<Reservations, Integer> capacity;


    @FXML
    private Button bookFlightButton; // Link to "Book a New Flight" button

    @FXML
    private Button cancelSelectedFlightButton; // Link to "Book a New Flight" button

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
        bookedFlightsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Allow multi-selection
        LogoutButton.setOnAction(this::logoutButtonClick);
        cancelSelectedFlightButton.setOnAction(this::handleCancelFlightButton);

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
                reservations.Reservation_ID,
                reservations.Username,
                reservations.Flight_Number,
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
                reservations.Reservation_ID = ?;
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, reservationId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ObservableList<Reservations> reservations = FXCollections.observableArrayList();

                // Add flight data to the list
                while (resultSet.next()) {
                    reservations.add(new Reservations(
                            resultSet.getString("Reservation_ID"),
                            resultSet.getString("Username"),
                            resultSet.getString("flight_number"),
                            resultSet.getString("departure_city"),
                            resultSet.getString("departure_time"),
                            resultSet.getString("destination_city"),
                            resultSet.getString("destination_time"),
                            resultSet.getString("airline"),
                            resultSet.getInt("available_seats"),
                            resultSet.getInt("capacity")
                    ));
                }

                if (reservations.isEmpty()) {
                    // If no flights are found, update the placeholder
                    bookedFlightsTable.setPlaceholder(new Label("Invalid reservation ID. No flights found."));
                } else {
                    // Populate the TableView with flight data
                    bookedFlightsTable.setItems(reservations);
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


@FXML
    private void handleCancelFlightButton(ActionEvent event) {
    ObservableList<Reservations> selectedReservations = bookedFlightsTable.getSelectionModel().getSelectedItems();

        if (selectedReservations.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select at least one flight to cancel.");
            return;
        }

    String deleteQuery = "DELETE FROM reservations WHERE Reservation_ID  = ?";


    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            for (Reservations reservation : selectedReservations) {
                String reservationId = reservation.getReservationId();

                preparedStatement.setString(1, reservationId);
                preparedStatement.addBatch(); // Batch the statements for efficiency
            }

            int[] rowsAffected = preparedStatement.executeBatch(); // Execute the batch delete
            System.out.println("Deleted " + rowsAffected.length + " reservations.");

            showAlert(Alert.AlertType.INFORMATION, "Success", "Selected reservations have been canceled.");
            bookedFlightsTable.getItems().removeAll(selectedReservations); // Remove the flights from the TableView
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while canceling reservations.");
        }
    }

        private void showAlert(Alert.AlertType alertType, String title, String message) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
}






