package com.example.demo;

public class ScanMessage {
    int code;
    String email;

    public ScanMessage(int code, String email) {
        this.code = code;
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}