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

public class ReservationsController {

    @FXML
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

    private void initialize() {

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
