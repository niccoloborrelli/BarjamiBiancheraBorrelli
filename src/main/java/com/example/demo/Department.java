package com.example.demo;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "departments")
public class Department {

    @Transient
    private List<Customer> customers_Inside;

    @Column(name = "dep_capability")
    private int depCapability;

    @EmbeddedId
    private DepId depId;

    public DepId getId() {
        return depId;
    }

    public void setDepType(DepId depID) {
        depId = depID;
    }

    public Department() {
    }
}
