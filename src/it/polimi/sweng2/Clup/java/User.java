package it.polimi.sweng2.Clup.java;

import javax.persistence.*;
import java.sql.Time;

@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@MappedSuperclass


public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    private String email;
    private String password;
    private Time meanVisitDuration;
    private Time delay;
    private String type;

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

    public Time getMeanVisitDuration() {
        return meanVisitDuration;
    }

    public void setMeanVisitDuration(Time meanVisitDuration) {
        this.meanVisitDuration = meanVisitDuration;
    }

    public Time getDelay() {
        return delay;
    }

    public void setDelay(Time delay) {
        this.delay = delay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
