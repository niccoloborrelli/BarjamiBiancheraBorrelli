package com.example.demo;

public class StoreMessage {
    String chain;
    String address;
    String email;

    public StoreMessage(String chain, String address, String email) {
        this.chain = chain;
        this.address = address;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
