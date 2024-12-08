package org.example.glider8;

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

private Connection connection;


    public void initialize() {
        connectToDatabase();



    }










    public void backToLoginClick() {
    }
}
*/