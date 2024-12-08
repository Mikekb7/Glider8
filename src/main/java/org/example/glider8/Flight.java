package org.example.glider8;

public class Flight {
    private String flightNumber;
    private String departureCity;
    private String departureTime;
    private String destinationCity;
    private String destinationTime;
    private String airline;
    private String availableSeats;
    private String capacity;

    public Flight(String flightNumber, String departureCity, String departureTime,
                  String destinationCity, String destinationTime, String airline, String  availableSeats, String capacity) {
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

}
