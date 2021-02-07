package com.example.demo;

public class ReservationStatusMessage {
    String chain;
    String address;
    String entryTime;
    String code;

    public ReservationStatusMessage() {
    }

    public ReservationStatusMessage(String chain, String address, String entryTime, String code) {
        this.chain = chain;
        this.address = address;
        this.entryTime = entryTime;
        this.code = code;
    }

    public String getChain() {
        return chain;
    }

    public String getAddress() {
        return address;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public String getCode() {
        return code;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
