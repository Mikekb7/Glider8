package org.example.glider8;
// Import necessary JavaFX and SQL classes
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReservationsController {
    // FXML elements defined in the corresponding FXML file
    @FXML
    private TextField usernameField; // Field to input username
    @FXML
    private Label welcomeLabel; // Field to input username
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
    // ObservableList to store booked flights
    private final ObservableList<Reservations> bookedFlights = FXCollections.observableArrayList(); // List of booked flights

    public void initialize() {
        connectToDatabase();

        // Bind the TableView columns to the Flight model class
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        departureCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        departureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
        destinationTime.setCellValueFactory(new PropertyValueFactory<>("destinationTime"));
        airline.setCellValueFactory(new PropertyValueFactory<>("airline"));
        availableSeats.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
// Set the placeholder text for the TableView
        bookedFlightsTable.setPlaceholder(new Label("You have no booked flights."));
        bookedFlightsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Allow multi-selection
        LogoutButton.setOnAction(this::logoutButtonClick);
        loadReservations();

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
    // Handle the "Enter" button action


    @FXML
    private void handleEnterAction() {// Get the reservation ID from the input field
        String usernameInput = usernameField.getText().trim();
        String reservationIdInput  = reservationIdField.getText().trim();
// Validate the input
        if (!usernameInput.isEmpty()) {
            fetchReservationsByUsername(usernameInput);
        } else if (!reservationIdInput.isEmpty()) {
            fetchReservationById(reservationIdInput);
        } else {
            bookedFlightsTable.setPlaceholder(new Label("Please enter a username or reservation ID."));
            bookedFlightsTable.getItems().clear();
        }
        }


        // Query the database to find flight information for the reservation
        /*String query = """
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
                bookedFlightsTable.setItems(reservations);

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
    }*/


    private void fetchReservationsByUsername(String username) {
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
                reservations.Username = ?;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Session.getUsername());
            executeQueryAndUpdateTable(preparedStatement);
        } catch (SQLException e) {
            bookedFlightsTable.setPlaceholder(new Label("An error occurred while fetching reservations."));
            e.printStackTrace();
        }
    }

    private void fetchReservationById(String reservationId) {
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
            executeQueryAndUpdateTable(preparedStatement);
        } catch (SQLException e) {
            bookedFlightsTable.setPlaceholder(new Label("An error occurred while fetching the reservation."));
            e.printStackTrace();
        }
    }

    private void executeQueryAndUpdateTable(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            bookedFlights.clear();
            while (resultSet.next()) {
                bookedFlights.add(new Reservations(
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
            bookedFlightsTable.setItems(bookedFlights);

            if (bookedFlights.isEmpty()) {
                bookedFlightsTable.setPlaceholder(new Label("No reservations found."));
            }
        }
    }



    // Handle the "Book a New Flight" button click
    @FXML
    protected void bookFlightButtonClick(ActionEvent event) {// Load the Booking.fxml file when the button is clicked
        System.out.println("Logout button clicked."); // Debug statement
        try {
            // Load the Booking.fxml file using FXMLLoader
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
    // Add a new booking to the TableView
    public void addBooking(Reservations reservation) {
        bookedFlights.add(reservation);
        bookedFlightsTable.setItems(bookedFlights); // Update the TableView with the new booking
    }// Update the TableView with the new booking

    // Handle the "Logout" button click
    @FXML
    protected void logoutButtonClick(ActionEvent event) {// Load the MainMenu.fxml file when the button is clicked
        System.out.println("Logout button clicked."); // Debug statement
        try {
            System.out.println(getClass().getResource("/org/example/glider8/MainMenu.fxml")); // Debug file path
            Parent mainMenuRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the current window
            stage.setScene(new Scene(mainMenuRoot)); // Set the scene to the MainMenu.fxml file
            stage.setTitle("Main Menu");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading MainMenu.fxml: " + e.getMessage());
        }

    }
    //
    @FXML
    protected void loginButtonClick( ) {
        System.out.println("Error loading MainMenu.fxml: ");// Debug statement

    }

    //
    @FXML
    private void handleCancelFlightButton(ActionEvent event) {// Get the selected reservations from the TableView
        ObservableList<Reservations> selectedReservations = bookedFlightsTable.getSelectionModel().getSelectedItems();
// Check if any reservations are selected
        if (selectedReservations.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select at least one flight to cancel.");
            return;
        }
// Prepare the SQL query to delete the selected reservations
        String deleteQuery = "DELETE FROM reservations WHERE Reservation_ID  = ?";
// Execute the batch delete operation
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            for (Reservations reservation : selectedReservations) {
                String reservationId = reservation.getReservationId();
// Set the reservation ID in the prepared statement
                preparedStatement.setString(1, reservationId);
                preparedStatement.addBatch(); // Batch the statements for efficiency
            }
// Execute the batch delete
            int[] rowsAffected = preparedStatement.executeBatch();
            System.out.println("Deleted " + rowsAffected.length + " reservations."); // Debug statement
// Show a success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Selected reservations have been canceled.");
            bookedFlightsTable.getItems().removeAll(selectedReservations); // Remove the flights from the TableView
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while canceling reservations.");
        }
    }
    // Show an alert with the specified type, title, and message
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadReservations() {
        String username = Session.getUsername(); // Fetch the logged-in user's username
        if (username == null) {
            bookedFlightsTable.setPlaceholder(new Label("Please log in to view your reservations."));
            return;
        }

        String query = """
        SELECT 
            reservations.Reservation_ID, 
            reservations.Flight_Number, 
            flights.departure_city, 
            flights.departure_time, 
            flights.destination_city, 
            flights.destination_time, 
            flights.airline
        FROM 
            reservations
        JOIN 
            flights 
        ON 
            reservations.flight_number = flights.flight_number
        WHERE 
            reservations.username = ?;
    """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ObservableList<Reservations> reservations = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    reservations.add(new Reservations(
                            resultSet.getString("Reservation_ID"),
                            username,
                            resultSet.getString("Flight_Number"),
                            resultSet.getString("departure_city"),
                            resultSet.getString("departure_time"),
                            resultSet.getString("destination_city"),
                            resultSet.getString("destination_time"),
                            resultSet.getString("airline"),
                            0, // Available seats (not used in reservations view)
                            0  // Capacity (not used in reservations view)
                    ));
                }

                bookedFlightsTable.setItems(reservations);

                if (reservations.isEmpty()) {
                    bookedFlightsTable.setPlaceholder(new Label("You have no booked flights."));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            bookedFlightsTable.setPlaceholder(new Label("An error occurred while fetching reservations."));
        }
    }





}






