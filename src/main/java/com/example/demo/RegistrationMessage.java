package com.example.demo;

public class RegistrationMessage {
    String username;
    String password;

    public RegistrationMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
