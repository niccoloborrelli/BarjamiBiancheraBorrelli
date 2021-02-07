package com.example.demo;

public class DeleteMessage {
    String chain;
    String address;
    String code;
    String email;

    public DeleteMessage(String chain, String address, String code, String email) {
        this.chain = chain;
        this.address = address;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
