package org.example.glider8;

public class Session {
    private static String username;
    private static String password;

    public static void setCredentials(String user, String pass) {
        username = user;
        password = pass;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static void clearSession() {
        username = null;
        password = null;
    }
}
