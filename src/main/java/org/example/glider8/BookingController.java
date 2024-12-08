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


