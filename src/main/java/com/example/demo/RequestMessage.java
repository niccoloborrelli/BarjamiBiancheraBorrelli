package com.example.demo;

import org.springframework.stereotype.Component;

import java.util.List;

public class RequestMessage {
    String username;
    String duration;
    String data;
    String chain;
    String address;
    String type;
    List<String> departments;
    String startingTime;

    public RequestMessage(String username, String duration, String data, String chain, String address, String type, List<String> departments, String startingTime) {
        this.username = username;
        this.duration = duration;
        this.data = data;
        this.chain = chain;
        this.address = address;
        this.type = type;
        this.departments = departments;
        this.startingTime = startingTime;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getData() {
        return data;
    }

    public String getUsername() {
        return username;
    }

    public String getChain() {
        return chain;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public List<String> getDepartments() {
        return departments;
    }
}
