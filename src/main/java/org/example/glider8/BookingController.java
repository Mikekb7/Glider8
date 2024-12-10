/*package org.example.glider8;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.example.glider8.DatabaseConnection.getConnection;

public class BookingController {
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
    private TableColumn<Flight, Integer> seatsColumn;
    @FXML
    private Button searchButton;
    @FXML
    private Button bookButton;
/*    @FXML
    private Button deleteButton;

/*
    public void initialize() {
        getConnection();

        // Bind columns to Flight properties
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        fromCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        toCityColumn.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        //seatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        resultsTable.setPlaceholder(new Label("No flights found."));

        // Enable the book button only when a row is selected
        resultsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            bookButton.setDisable(newSelection == null);
            deleteButton.setDisable(newSelection == null);
        });

        // Disable the book button by default
        bookButton.setDisable(true);
        deleteButton.setDisable(true);

        searchButton.setOnAction(this::handleSearchFlights);
        bookButton.setOnAction(this::handleBookFlight);
        deleteButton.setOnAction(this::handleDeleteFlight);
    }

    @FXML
    private void handleSearchFlights(ActionEvent event) {
        String fromCity = fromCityField.getText().trim();
        String toCity = toCityField.getText().trim();
        String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
        String time = timeField.getText().trim();

        // Validate input fields
        if (fromCity.isEmpty() || toCity.isEmpty() || date.isEmpty()) {
            resultsTable.setPlaceholder(new Label("Please fill out all required fields."));
            resultsTable.getItems().clear();
            return;
        }

        // Clear previous search results
        resultsTable.getItems().clear();

        // Query the database
        String query = """
                SELECT flight_number, departure_city, destination_city, flight_date, flight_time, available_seats
                FROM flights
                WHERE departure_city = ? AND destination_city = ? AND flight_date = ? AND flight_time >= ?
                """;

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, fromCity);
            preparedStatement.setString(2, toCity);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, time);

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<Flight> flights = FXCollections.observableArrayList();

            while (resultSet.next()) {
                flights.add(new Flight(
                        resultSet.getString("flight_number"),
                        resultSet.getString("departure_city"),
                        resultSet.getString("destination_city"),
                        resultSet.getString("flight_date"),
                        resultSet.getString("flight_time"),
                        resultSet.getInt("available_seats")
                ));
            }

            if (flights.isEmpty()) {
                resultsTable.setPlaceholder(new Label("No flights found for the specified criteria."));
            } else {
                resultsTable.setItems(flights);
            }
        } catch (Exception e) {
            resultsTable.setPlaceholder(new Label("An error occurred while searching for flights."));
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBookFlight(ActionEvent event) {
        Flight selectedFlight = resultsTable.getSelectionModel().getSelectedItem();

        if (selectedFlight == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a flight to book.");
            return;
        }

        if (selectedFlight.getAvailableSeats() <= 0) {
            showAlert(Alert.AlertType.WARNING, "Flight Full", "No seats are available for this flight.");
            return;
        }

        try {
            // Check if the user has already booked this flight
            String checkQuery = "SELECT * FROM reservations WHERE username = ? AND flight_number = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, currentUser);
                checkStmt.setString(2, selectedFlight.getFlightNumber());
                ResultSet checkResult = checkStmt.executeQuery();
                if (checkResult.next()) {
                    showAlert(Alert.AlertType.WARNING, "Duplicate Booking", "You have already booked this flight.");
                    return;
                }
            }

            // Book the flight
            String bookQuery = "INSERT INTO reservations (username, flight_number) VALUES (?, ?)";
            try (PreparedStatement bookStmt = connection.prepareStatement(bookQuery)) {
                bookStmt.setString(1, currentUser);
                bookStmt.setString(2, selectedFlight.getFlightNumber());
                bookStmt.executeUpdate();
            }

            // Update available seats
            String updateSeatsQuery = "UPDATE flights SET available_seats = available_seats - 1 WHERE flight_number = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSeatsQuery)) {
                updateStmt.setString(1, selectedFlight.getFlightNumber());
                updateStmt.executeUpdate();
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Flight booked successfully!");
            handleSearchFlights(null); // Refresh results
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while booking the flight.");
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
}
/*
    public void backToLoginClick() {
    }

}*/

package org.example.glider8;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingController {

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

    private Connection connection;

    public void initialize() {
        connectToDatabase();

        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        fromCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        toCityColumn.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        resultsTable.setPlaceholder(new Label("No flights found."));

        searchButton.setOnAction(event -> searchFlights());
        bookButton.setOnAction(event -> bookFlightButtonClick());
        backToLoginButton.setOnAction(event -> backToLoginClick());
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
    private void searchFlights() {
        String fromCity = fromCityField.getText().trim();
        String toCity = toCityField.getText().trim();
        String date = (datePicker.getValue() != null) ? datePicker.getValue().toString() : "";

        if (fromCity.isEmpty() || toCity.isEmpty() || date.isEmpty()) {
            resultsTable.setPlaceholder(new Label("Please fill in all fields to search for flights."));
            resultsTable.getItems().clear();
            return;
        }

        // Clear previous search results
        resultsTable.getItems().clear();

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
            preparedStatement.setString(1, fromCity);
            preparedStatement.setString(2, toCity);
            preparedStatement.setString(3, date);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ObservableList<Flight> flights = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    flights.add(new Flight(
                            resultSet.getString("flight_number"),
                            resultSet.getString("departure_city"),
                            resultSet.getString("departure_date"),
                            resultSet.getString("destination_city"),
                            resultSet.getString("destination_time"),
                            resultSet.getString("airline"),
                            resultSet.getString("available_seats"),
                            resultSet.getString("capacity")
                    ));
                }

                if (flights.isEmpty()) {
                    resultsTable.setPlaceholder(new Label("Sorry, no flights found :("));
                } else {
                    resultsTable.setItems(flights);
                }
            }
        } catch (Exception e) {
            resultsTable.setPlaceholder(new Label("An error occurred while searching for flights."));
            e.printStackTrace();
        }
    }

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

        String bookQuery = "UPDATE flights SET available_seats = available_seats - 1 WHERE flight_number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(bookQuery)) {
            preparedStatement.setString(1, selectedFlight.getFlightNumber());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlertAndNavigate("Booking Confirmed", "Your flight has been booked successfully.", selectedFlight);
            } else {
                showAlert(Alert.AlertType.ERROR, "Booking Failed", "An error occurred while booking the flight.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the flight information.");
            e.printStackTrace();
        }
    }

    private void showAlertAndNavigate(String title, String content, Flight bookedFlight) {
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

    private void navigateToReservations(Flight bookedFlight) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/glider8/Reservations.fxml"));
            Parent reservationsRoot = loader.load();

            ReservationsController reservationsController = loader.getController();
            reservationsController.addBooking(bookedFlight);

            Stage stage = (Stage) bookButton.getScene().getWindow();
            stage.setScene(new Scene(reservationsRoot));
            stage.setTitle("Your Reservations");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Reservations.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void backToLoginClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/glider8/MainMenu.fxml"));
            Parent mainMenuRoot = loader.load();

            Stage stage = (Stage) backToLoginButton.getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot));
            stage.setTitle("Main Menu - Glider");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading MainMenu.fxml: " + e.getMessage());
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
