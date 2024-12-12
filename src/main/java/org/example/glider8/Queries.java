package org.example.glider8;

public class Queries {// SQL queries for the application
    public static final String login = "SELECT Username, Password FROM user WHERE Username = ? AND Password = ?";
    public static final String insertQuery = """
    INSERT INTO user 
    (username, password, first_name, last_name, email, address, city, state, zipcode, securityanswer, ssn)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

}
