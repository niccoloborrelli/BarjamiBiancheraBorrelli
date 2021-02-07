package com.example.demo;

public class LoginMessage {
    String username;
    String password;
    String type;

    public LoginMessage(String username, String password, String type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
