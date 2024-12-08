package org.example.glider8;

public class Flight {
    private String flightNumber;
    private String departureCity;
    private String departureTime;
    private String destinationCity;
    private String destinationTime;
    private String airline;

    public Flight(String flightNumber, String departureCity, String departureTime,
                  String destinationCity, String destinationTime, String airline) {
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.departureTime = departureTime;
        this.destinationCity = destinationCity;
        this.destinationTime = destinationTime;
        this.airline = airline;
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
}
