package org.example.glider8;

public class Flight {
    // Fields for the Flight class
    private String flightNumber;
    private String departureCity;
    private String departureTime;
    private String destinationCity;
    private String destinationTime;
    private String airline;
    private String availableSeats;
    private String capacity;

// Constructor for the Flight class
    public Flight(String flightNumber, String departureCity, String departureTime,
                  String destinationCity, String destinationTime, String airline, String  availableSeats, String capacity) {// Initialize the fields with the provided values
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.departureTime = departureTime;
        this.destinationCity = destinationCity;
        this.destinationTime = destinationTime;
        this.airline = airline;
        this.availableSeats = availableSeats;
        this.capacity = capacity;
    }

    public String getFlightNumber() {
        return flightNumber;
    }// Getter methods for the fields

    public String getDepartureCity() {
        return departureCity;
    }// Getter methods for the fields

    public String getDepartureTime() {
        return departureTime;
    } // Getter methods for the fields

    public String getDestinationCity() {
        return destinationCity;
    }//

    public String getDestinationTime() {
        return destinationTime;
    }// Getter methods for the fields

    public String getAirline() {
        return airline;
    } // Getter methods for the fields

    public String getAvailableSeats() {
        return availableSeats;
    } // Getter methods for the fields

    public String getCapacity() {
        return capacity;
    } // Getter methods for the fields

}
