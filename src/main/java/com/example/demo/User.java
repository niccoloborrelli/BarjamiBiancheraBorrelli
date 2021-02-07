package com.example.demo;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@NamedQuery(name = "User.findById", query = "select u from User u where u.userID = :query" )
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    private String email;
    private String password;

    private long mean_visit_duration;
    private long delay;

    @Column(name = "number_of_visit")
    private int numberOfVisit;

    @Column(insertable = false, updatable = false)
    private String type;

    private int storeID;

    public int getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getMeanVisitDuration() {
        return mean_visit_duration;
    }

    public void setMeanVisitDuration(long mean_visit_duration) {
        this.mean_visit_duration = mean_visit_duration;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public int getNumberOfVisit() {
        return numberOfVisit;
    }

    public void setNumberOfVisit(int numberOfVisit) {
        this.numberOfVisit = numberOfVisit;
    }

    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }
}
