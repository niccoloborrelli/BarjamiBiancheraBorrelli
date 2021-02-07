package com.example.demo;

public class StoreCreationMessage {
    String chain;
    String address;
    String openingTime;
    String closureTime;

    public StoreCreationMessage(String chain, String address, String openingTime, String closureTime) {
        this.chain = chain;
        this.address = address;
        this.openingTime = openingTime;
        this.closureTime = closureTime;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosureTime() {
        return closureTime;
    }

    public void setClosureTime(String closureTime) {
        this.closureTime = closureTime;
    }
}
