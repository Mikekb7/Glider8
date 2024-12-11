package org.example.glider8;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class BookingController {
    //     // FXML elements defined in the corresponding FXML file
    @FXML
    private TextField fromCityField;
    @FXML
    private TextField toCityField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeField;
    @FXML
    private TableView<Flight> resultsTable;
    @FXML
    private TableColumn<Flight, String> flightNumberColumn;
    @FXML
    private TableColumn<Flight, String> fromCityColumn;
    @FXML
    private TableColumn<Flight, String> toCityColumn;
    @FXML
    private TableColumn<Flight, String> dateColumn;
    @FXML
    private TableColumn<Flight, String> timeColumn;
    @FXML
    private TableColumn<Flight, String> seatsColumn;
    @FXML
    private Button searchButton;
    @FXML
    private Button bookButton;
    @FXML
    private Button backToLoginButton;
    @FXML
    private Button cancelSelectedFlightButton; // Link to "Book a New Flight" button
    @FXML
    private TableView<Reservations> bookedFlightsTable; // TableView to display flights

    private Connection connection;

    public void initialize() {
        // Connect to the database
        connectToDatabase();

        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        fromCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        toCityColumn.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
//         // Set the placeholder text for the results table
        resultsTable.setPlaceholder(new Label("No flights found."));

        searchButton.setOnAction(event -> searchFlights());
        bookButton.setOnAction(event -> bookFlightButtonClick());
        backToLoginButton.setOnAction(event -> backToLoginClick());
        //cancelSelectedFlightButton.setOnAction(this::handleCancelFlightButton);

    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://gliderserver.mysql.database.azure.com:3306/gliderdatabase?useSSL=true&serverTimezone=UTC";
        String username = "glider";
        String password = "Gpassword123";

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully.");
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    @FXML
    private void searchFlights() {// Handle the search button click event
        String fromCity = fromCityField.getText().trim(); // Get the text from the fromCityField and remove any leading or trailing whitespace
        String toCity = toCityField.getText().trim();
        String date = (datePicker.getValue() != null) ? datePicker.getValue().toString() : ""; // Get the text from the datePicker and convert it to a string

        if (fromCity.isEmpty() || toCity.isEmpty() || date.isEmpty()) {
            resultsTable.setPlaceholder(new Label("Please fill in all fields to search for flights."));
            resultsTable.getItems().clear();
            return;
        }
// Clear previous search results
        resultsTable.getItems().clear();
//         // Query to search for flights based on the user input
        String query = """
            SELECT 
                flight_number, 
                departure_city, 
                DATE(departure_time) AS departure_date, 
                destination_city, 
                destination_time, 
                airline, 
                available_seats, 
                capacity
            FROM flights
            WHERE departure_city = ? 
              AND destination_city = ? 
              AND DATE(departure_time) = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, fromCity); // Set the first parameter in the query to the fromCity
            preparedStatement.setString(2, toCity);// Set the second parameter in the query to the toCity
            preparedStatement.setString(3, date);// Set the third parameter in the query to the date

            try (ResultSet resultSet = preparedStatement.executeQuery()) {// Execute the query and store the results in a ResultSet
                ObservableList<Flight> flights = FXCollections.observableArrayList(); // Create an observable list to store the flights

                while (resultSet.next()) {// Loop through the result set and add each flight to the observable list
                    flights.add(new Flight(
                            resultSet.getString("flight_number"), // Get the flight number from the result set
                            resultSet.getString("departure_city"),
                            resultSet.getString("departure_date"),
                            resultSet.getString("destination_city"),
                            resultSet.getString("destination_time"),
                            resultSet.getString("airline"),
                            resultSet.getString("available_seats"),
                            resultSet.getString("capacity")
                    ));
                }

                if (flights.isEmpty()) { // Check if the list of flights is empty
                    resultsTable.setPlaceholder(new Label("Sorry, no flights found :("));
                } else {
                    resultsTable.setItems(flights); // Set the items in the results table to the list of flights
                }
            }
        } catch (Exception e) {
            resultsTable.setPlaceholder(new Label("An error occurred while searching for flights.")); // Set the placeholder text for the results table
            e.printStackTrace();
        }
    }

    @FXML
    private void bookFlightButtonClick() {// Handle the book button click event
        Flight selectedFlight = resultsTable.getSelectionModel().getSelectedItem(); // Get the selected flight from the results table
        if (selectedFlight == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a flight to book."); // Show a warning if no flight is selected
            return;
        }

        if (Integer.parseInt(selectedFlight.getAvailableSeats()) <= 0) { // Check if the available seats for the selected flight are less than or equal to 0
            showAlert(Alert.AlertType.INFORMATION, "Flight Full", "This flight is fully booked.");
            return;// Show an information alert if the flight is fully booked
        }

        String username = Session.getUsername();
        if (username == null) {
            showAlert(Alert.AlertType.ERROR, "User Not Logged In", "Please log in to book a flight.");
            return;
        }



        String insertReservationQuery = "INSERT INTO reservations (username, flight_number) VALUES (?, ?)";
        String bookQuery = "UPDATE flights SET available_seats = available_seats - 1 WHERE flight_number = ?"; // Query to book the selected flight
        try (PreparedStatement reservationStatement = connection.prepareStatement(insertReservationQuery);
             PreparedStatement flightUpdateStatement = connection.prepareStatement(bookQuery)) {


            //preparedStatement.setString(1, selectedFlight.getFlightNumber()); // Set the flight number in the query to the selected flight number
            // Insert reservation
            reservationStatement.setString(1, username);
            reservationStatement.setString(2, selectedFlight.getFlightNumber());
            reservationStatement.executeUpdate();

            // Update flight availability
            flightUpdateStatement.setString(1, selectedFlight.getFlightNumber());
            flightUpdateStatement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Booking Confirmed", "Your flight has been booked successfully!");
            selectedFlight.setAvailableSeats(String.valueOf(Integer.parseInt(selectedFlight.getAvailableSeats()) - 1));
            resultsTable.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while booking the flight.");
        }
    }
    /*
        @FXML
        private void bookFlightButtonClick() {
            Flight selectedFlight = resultsTable.getSelectionModel().getSelectedItem();

            if (selectedFlight == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a flight to book.");
                return;
            }

            if (Integer.parseInt(selectedFlight.getAvailableSeats()) <= 0) {
                showAlert(Alert.AlertType.INFORMATION, "Flight Full", "This flight is fully booked.");
                return;
            }

            // Insert reservation into the database
            String insertReservationQuery = "INSERT INTO reservations (username, flight_number) VALUES (?, ?)";
            String updateFlightQuery = "UPDATE flights SET available_seats = available_seats - 1 WHERE flight_number = ?";

            try (PreparedStatement reservationStatement = connection.prepareStatement(insertReservationQuery);
                 PreparedStatement flightUpdateStatement = connection.prepareStatement(updateFlightQuery)) {

                // Insert reservation
                reservationStatement.setString(1, "user"); // Replace with the actual username (dynamic if needed)
                reservationStatement.setString(2, selectedFlight.getFlightNumber());
                reservationStatement.executeUpdate();

                // Update flight availability
                flightUpdateStatement.setString(1, selectedFlight.getFlightNumber());
                int rowsUpdated = flightUpdateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Booking Confirmed", "Your flight has been booked successfully!");
                    selectedFlight.setAvailableSeats(String.valueOf(Integer.parseInt(selectedFlight.getAvailableSeats()) - 1));
                    resultsTable.refresh();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Booking Failed", "An error occurred while updating the flight information.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while booking the flight.");
            }
        }
    */
    private void showAlertAndNavigate(String title, String content, Reservations bookedFlight) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                navigateToReservations(bookedFlight);
            }
        });
    }
    //     // Method to navigate to the Reservations screen
    //lo
    @FXML
    private void navigateToReservations(Reservations bookedFlight) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/glider8/Reservations.fxml"));
            Parent reservationsRoot = loader.load();

            ReservationsController reservationsController = loader.getController(); // Get the controller for the Reservations screen
            reservationsController.addBooking(bookedFlight); //

            Stage stage = (Stage) bookButton.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(reservationsRoot)); // Set the scene to the reservationsRoot
            stage.setTitle("Your Reservations");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Reservations.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void backToLoginClick() { // Handle the back to login button click event
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/glider8/MainMenu.fxml"));
            Parent mainMenuRoot = loader.load(); // Load the Main Menu FXML file

            Stage stage = (Stage) backToLoginButton.getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot)); // Set the scene to the mainMenuRoot
            stage.setTitle("Main Menu - Glider");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading MainMenu.fxml: " + e.getMessage());
        }
    }


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


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
