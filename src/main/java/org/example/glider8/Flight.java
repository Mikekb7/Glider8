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
                  String destinationCity, String destinationTime, String airline, String availableSeats, String capacity) {
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.departureTime = departureTime;
        this.destinationCity = destinationCity;
        this.destinationTime = destinationTime;
        this.airline = airline;
        this.availableSeats = availableSeats;
        this.capacity = capacity;
    }

    // Getter methods
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public String getDestinationTime() {
        return destinationTime;
    }

    public String getAirline() {
        return airline;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public String getCapacity() {
        return capacity;
    }

    // Setter methods
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public void setDestinationTime(String destinationTime) {
        this.destinationTime = destinationTime;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}
