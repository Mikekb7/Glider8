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
import org.example.glider8.common.Actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminPageController {

    @FXML
    private TextField flightNumberField, departureCityField, departureTimeField,
            destinationCityField, destinationTimeField, airlineField, availableSeatsField, capacityField;

    @FXML
    private Button addFlightButton, allFlights, cancelFlightButton, updateFlightButton;
    @FXML
    private Button backToLoginButton;

    @FXML
    private TableView<Flight> bookedFlightsTable;

    @FXML
    private TableColumn<Flight, String> flightNumberColumn, departureCityColumn, departureTimeColumn,
            destinationCityColumn, destinationTimeColumn, airlineColumn;

    @FXML
    private TableColumn<Flight, String> availableSeatsColumn, capacityColumn;

    private ObservableList<Flight> flightList = FXCollections.observableArrayList();
    private Connection connection;


    public void initialize() {
        connectToDatabase();

        // Bind table columns to the Flight model
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        departureCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        destinationCityColumn.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
        destinationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("destinationTime"));
        airlineColumn.setCellValueFactory(new PropertyValueFactory<>("airline"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        // Attach actions to buttons
        allFlights.setOnAction(event -> viewAllFlights());
        addFlightButton.setOnAction(event -> addFlight());
        updateFlightButton.setOnAction(event -> handleUpdateButtonClick());
        //deleteFlightButton.setOnAction(event -> deleteFlight());
        //backToLoginButton.setOnAction(this::backToLoginButtonClick);

        bookedFlightsTable.setPlaceholder(new Label("No flights found."));


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

    private void fetchAllFlights() {
        flightList.clear();
        String query = "SELECT * FROM flights";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                flightList.add(new Flight(
                        resultSet.getString("flight_number"),
                        resultSet.getString("departure_city"),
                        resultSet.getString("departure_time"),
                        resultSet.getString("destination_city"),
                        resultSet.getString("destination_time"),
                        resultSet.getString("airline"),
                        String.valueOf(resultSet.getInt("available_seats")), // Convert int to String
                        String.valueOf(resultSet.getInt("capacity"))
                ));
            }

            bookedFlightsTable.setItems(flightList);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch flights.");
            e.printStackTrace();
        }
    }
@FXML
    private void addFlight() {
        String query = "INSERT INTO flights (flight_number, departure_city, departure_time, destination_city, destination_time, airline, available_seats, capacity) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumberField.getText());
            preparedStatement.setString(2, departureCityField.getText());
            preparedStatement.setString(3, departureTimeField.getText());
            preparedStatement.setString(4, destinationCityField.getText());
            preparedStatement.setString(5, destinationTimeField.getText());
            preparedStatement.setString(6, airlineField.getText());
            preparedStatement.setInt(7, Integer.parseInt(availableSeatsField.getText()));
            preparedStatement.setInt(8, Integer.parseInt(capacityField.getText()));

            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Flight added successfully.");
            fetchAllFlights();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the flight.");
            e.printStackTrace();
        }
    }
@FXML
    private void updateFlight() {
        String query = "UPDATE flights SET departure_city = ?, departure_time = ?, destination_city = ?, destination_time = ?, airline = ?, available_seats = ?, capacity = ? WHERE flight_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, departureCityField.getText());
            preparedStatement.setString(2, departureTimeField.getText());
            preparedStatement.setString(3, destinationCityField.getText());
            preparedStatement.setString(4, destinationTimeField.getText());
            preparedStatement.setString(5, airlineField.getText());
            preparedStatement.setInt(6, Integer.parseInt(availableSeatsField.getText()));
            preparedStatement.setInt(7, Integer.parseInt(capacityField.getText()));
            preparedStatement.setString(8, flightNumberField.getText());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Flight updated successfully.");
                fetchAllFlights();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Flight not found.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the flight.");
            e.printStackTrace();
        }
    }
@FXML
    private void deleteFlight() {
        String query = "DELETE FROM flights WHERE flight_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, flightNumberField.getText());

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Flight deleted successfully.");
                fetchAllFlights();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Flight not found.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the flight.");
            e.printStackTrace();
        }
    }
    @FXML
    private void backToLoginButtonClick(ActionEvent event) {
        Actions.loadFXML(event, "/org/example/glider8/MainMenu.fxml", "Main Menu"); // Load the Main Menu FXML using the Actions class
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void viewAllFlights() {
        String query = "SELECT * FROM flights";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            flightList.clear(); // Clear the current list before populating
            while (resultSet.next()) {
                flightList.add(new Flight(
                        resultSet.getString("flight_number"),
                        resultSet.getString("departure_city"),
                        resultSet.getString("departure_time"),
                        resultSet.getString("destination_city"),
                        resultSet.getString("destination_time"),
                        resultSet.getString("airline"),
                        String.valueOf(resultSet.getInt("available_seats")),
                        String.valueOf(resultSet.getInt("capacity"))
                ));
            }

            bookedFlightsTable.setItems(flightList);

            if (flightList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Flights Found", "No flights are currently available.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch flights.");
            e.printStackTrace();
        }
    }

    @FXML
    private void updateFlightAttribute(String flightNumber, String column, String newValue) {
        // List of valid columns to update
        final String[] validColumns = {
                "departure_city", "departure_time", "destination_city",
                "destination_time", "airline", "available_seats", "capacity"
        };

        // Validate the column name
        boolean isValidColumn = false;
        for (String validColumn : validColumns) {
            if (validColumn.equals(column)) {
                isValidColumn = true;
                break;
            }
        }

        if (!isValidColumn) {
            showAlert(Alert.AlertType.WARNING, "Invalid Column", "The specified column is not valid for updates.");
            return;
        }

        // Validate input
        if (flightNumber == null || flightNumber.isEmpty() || newValue == null || newValue.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please provide a valid flight number and new value.");
            return;
        }

        // SQL query to update a specific column
        String query = "UPDATE flights SET " + column + " = ? WHERE flight_number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newValue); // Set the new value
            preparedStatement.setString(2, flightNumber); // Set the flight number

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Flight updated successfully.");
                fetchAllFlights(); // Refresh the table to show updated data
            } else {
                showAlert(Alert.AlertType.WARNING, "Not Found", "No flight found with the specified flight number.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the flight.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateButtonClick() {
        String flightNumber = flightNumberField.getText().trim();
        String column = "departure_city"; // Example: column to update
        String newValue = departureCityField.getText().trim();

        updateFlightAttribute(flightNumber, column, newValue);
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



}
