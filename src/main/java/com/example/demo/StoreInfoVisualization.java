package com.example.demo;

import javafx.scene.image.ImageView;

public class StoreInfoVisualization {
    String chain;
    String address;
    String openTime;
    String closureTime;

    public StoreInfoVisualization() {

    }

    public String getChain() {
        return chain;
    }

    public String getAddress() {
        return address;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getClosureTime() {
        return closureTime;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setClosureTime(String closureTime) {
        this.closureTime = closureTime;
    }
}
