package com.example.demo;

import javax.persistence.*;

import java.sql.Date;
import java.sql.Time;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Table(name = "reservations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "res_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resID")
    private int id;
    private int userID;

    @Column(name = "res_type", insertable = false, updatable = false)
    private String resType;
    private int code;
    private int storeID;

    @Column(name = "starting_time")
    private long startingTime;

    private long duration;

    private String status;

    @Transient
    private Itinerary itinerary;

    @Column(name = "entry_time")
    private long entryTime;

    @Column(name = "exit_time")
    private long exitTime;

    @Column(name = "time_of_insertion")
    private long timeOfInsertion;
    private Date date;

    public int getId() {
        return id;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public long getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(long startingTime) {
        this.startingTime = startingTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
    }

    public long getExitTime() {
        return exitTime;
    }

    public void setExitTime(long exitTime) {
        this.exitTime = exitTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTimeOfInsertion() {
        return timeOfInsertion;
    }

    public void setTimeOfInsertion(long timeOfInsertion) {
        this.timeOfInsertion = timeOfInsertion;
    }
}

