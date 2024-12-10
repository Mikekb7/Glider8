package org.example.glider8;

public class Reservations {
    // Fields for the Reservations class
    private final String reservationId;
    private final String username;      // Other reservation fields
    private final String flightNumber;
    private final String departureCity;
    private String departureTime;
    private String destinationCity;
    private String destinationTime;
    private String airline;
    private int availableSeats;
    private int capacity;

    //Constructor for the Reservations class
    public Reservations(String reservationId, String username, String flightNumber, String departureCity, String departureTime,
                        String destinationCity, String destinationTime, String airline, int availableSeats, int capacity) { // Initialize the fields with the provided values
        this.reservationId = reservationId;
        this.username = username;
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.departureTime = departureTime;
        this.destinationCity = destinationCity;
        this.destinationTime = destinationTime;
        this.airline = airline;
        this.availableSeats = availableSeats;
        this.capacity = capacity;
    }


    // Getter methods for the fields for encapsulation and data retrieval because
    // of the PropertyValuesFactory uses the getters to extract values from the
    // Reservations objects and display them in the TableView.
    public String getReservationId() {// Getter methods for the fields
        return reservationId;
    }

    public String getUsername() { // Getter methods for the fields
        return username;
    }

    public String getFlightNumber() { // Getter methods for the fields
        return flightNumber;
    }

    public String getDepartureCity() { // Getter methods for the fields
        return departureCity;
    }

    public String getDepartureTime() {// Getter methods for the fields
        return departureTime;
    }

    public String getDestinationCity() {// Getter methods for the fields
        return destinationCity;
    }

    public String getDestinationTime() { // Getter methods for the fields
        return destinationTime;
    }

    public String getAirline() { // Getter methods for the fields
        return airline;
    }

    public int getAvailableSeats() { // Getter methods for the fields
        return availableSeats;
    }

    public int getCapacity() { //
        return capacity;
    }
}



