package it.polimi.sweng2.Clup.java;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "stores")
@NamedQueries({
        @NamedQuery(name = "Store.findStoreById", query = "select s from Store s where s.storeID = :query"),
        @NamedQuery(name = "Store.findStoreByChain", query = "select s from Store s where s.chain = :query")
})

public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeID;

    private String chain;
    private String address;
    private Time avgVisitDuration;
    private String name;
    private int maxCapability;
    private Time closureTime;
    private Time startingTime;
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
    private List<Customer> customers_Inside = new ArrayList<Customer>();

    //the control on the max Capability will be performed outside of this method
    public boolean addCustomer(Customer customer){
        if(customers_Inside.contains(customer))
            return false;
        else
            customers_Inside.add(customer);
        return true;
    }

    //the control on the max Capability will be performed outside of this method
    public boolean removeCustomer(Customer customer){
        if(customers_Inside.contains(customer)) {
            customers_Inside.remove(customer);
            return true;
        }
        else
            return false;
    }

    public boolean isFull(){
        if(customers_Inside.size() == maxCapability)
            return true;
        else
            return false;
    }

    public void addDepartment(Department department){
        departments.add(department);
    }

    public void removeDepartment(Department department){
        departments.remove(department);
    }

    public int getId() {
        return storeID;
    }

    public List<Customer> getCustomers_Inside() {
        return customers_Inside;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain){
        this.chain = chain;
    }

    public Position getPosition() {
        return position;
    }

     public void setPosition(Position position){
        this.position = position;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public int getMaxCapability() {
        return maxCapability;
    }

    public void setMaxCapability(int maxCapability){
        this.maxCapability = maxCapability;
    }

    public Time getClosureTime() {
        return closureTime;
    }

    public Time getStartingTime() {
        return startingTime;
    }

    public void setClosureTime(Time closureTime) {
        this.closureTime = closureTime;
    }

    public void setStartingTime(Time openingTime) {
        this.startingTime = openingTime;
    }

    public void setCustomers_Inside(ArrayList<Customer> customers_Inside) {
        this.customers_Inside = customers_Inside;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Time getAverageVisitDuration() {
        return avgVisitDuration;
    }

    public void setAverageVisitDuration(Time averageVisitDuration) {
        this.avgVisitDuration = averageVisitDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }
}
