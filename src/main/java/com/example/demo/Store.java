package com.example.demo;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeID;

    private String chain;
    private String address;
    @Column(name = "avg_visit_duration")
    private long avgVisitDuration;

    @Column(name = "max_capability")
    private int maxCapability;

    @Column(name = "closure_time")
    private long closureTime;

    @Column(name = "starting_time")
    private long startingTime;

    @Column(name = "number_of_visit")
    private int numberOfVisit;
    /*
    @ManyToOne
    @JoinTable(name = "distance", joinColumns = @JoinColumn(name = "idStore1"))
    private Store mainStore;

    @OneToMany(mappedBy = "mainStore")
    private List<Store> nearStore;

     */

    @Transient
    private Position position;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "storeid")
    private List<Department> departments = new ArrayList<Department>();

    @Transient
    private List<Reservation> customers_Inside = new ArrayList<>();

    //the control on the max Capability will be performed outside of this method


    public boolean isFull() {
        if (customers_Inside.size() == maxCapability)
            return true;
        else
            return false;
    }

    public void addDepartment(Department department) {
        departments.add(department);
    }

    public void removeDepartment(Department department) {
        departments.remove(department);
    }

    public int getId() {
        return storeID;
    }

    public List<Reservation> getCustomers_Inside() {
        return customers_Inside;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public int getMaxCapability() {
        return maxCapability;
    }

    public void setMaxCapability(int maxCapability) {
        this.maxCapability = maxCapability;
    }

    public long getClosureTime() {
        return closureTime;
    }

    public long getStartingTime() {
        return startingTime;
    }

    public void setClosureTime(long closureTime) {
        this.closureTime = closureTime;
    }

    public void setStartingTime(long openingTime) {
        this.startingTime = openingTime;
    }

    public void setCustomers_Inside(ArrayList<Reservation> customers_Inside) {
        this.customers_Inside = customers_Inside;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getAverageVisitDuration() {
        return avgVisitDuration;
    }

    public void setAverageVisitDuration(long averageVisitDuration) {
        this.avgVisitDuration = averageVisitDuration;
    }

    public int getNumberOfVisit() {
        return numberOfVisit;
    }

    public void setNumberOfVisit(int numberOfVisit) {
        this.numberOfVisit = numberOfVisit;
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }
}
