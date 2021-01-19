package it.polimi.sweng2.Clup.java;

import javax.persistence.*;

import java.sql.Time;

import static javax.persistence.InheritanceType.SINGLE_TABLE;


@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "resType")
@MappedSuperclass
//@NamedQuery(name = "Reservation.findByUser", query = "select r from Reservation r where r.userID = :query")
//@NamedQuery(name = "Reservation.findByStore", query = "select r from Reservation r where r.storeID = :query ")
//@NamedQuery(name = "Reservation.findByResType", query = "select r from Reservation r where r.resType = :query")
public abstract class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resID")
    private int id;
    private int userID;
    private String resType;
    private int code;
    private int storeID;

    @Column(name = "time")
    private Time startingTime;

    private Time duration;

    @Column(name = "accepted/rejected")
    private String status;

    @Transient
    private Itinerary itinerary;


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

    public Time getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Time startingTime) {
        this.startingTime = startingTime;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

