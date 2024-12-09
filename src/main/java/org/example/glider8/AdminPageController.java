package org.example.glider8;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class AdminPageController {
    @FXML
    private Button logoutButton;

    @FXML
    private Button viewFlightsButton;

    @FXML
    private Button addFlightButton;

    @FXML
    private Button deleteFlightButton;

    @FXML
    private Button updateFlightButton;

    @FXML
    private TextField flightNumberField;

    @FXML
    private TextField departureCityField;

    @FXML
    private TextField departureTimeField;

    @FXML
    private TextField destinationCityField;

    @FXML
    private TextField destinationTimeField;

    @FXML
    private TextField airlineField;

    @FXML
    private TextField availableSeatsField;

    @FXML
    private TextField capacityField;

    @FXML
    private TableView<Flight> resultsTable; // Table to display search results

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

    private Connection connection;


    public void initialize() {
        connectToDatabase();

        // Bind table columns to the Flight model
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        fromCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        toCityColumn.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        // Set default placeholder for the results table
        resultsTable.setPlaceholder(new javafx.scene.control.Label("No flights found."));

        // Attach actions to buttons

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

}
