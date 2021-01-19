package it.polimi.sweng2.Clup.java;

import java.sql.Time;

public class Position {
    private float latitude;
    private float longitude;
    private Time timeToReach;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
