package org.example.glider8;

public class Reservations {
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

    //Constructor
    public Reservations(String reservationId, String username, String flightNumber, String departureCity, String departureTime,
                        String destinationCity, String destinationTime, String airline, int availableSeats, int capacity) {
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

    public String getReservationId() {
        return reservationId;
    }

    public String getUsername() {
        return username;
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

    public int getAvailableSeats() {
        return availableSeats;
    }

    public int getCapacity() {
        return capacity;
    }
}



