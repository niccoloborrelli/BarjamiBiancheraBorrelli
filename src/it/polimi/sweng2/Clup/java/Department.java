package it.polimi.sweng2.Clup.java;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "departments")
public class Department {

    @Transient
    private List<Customer> customers_Inside;

    private int depCapability;

    @EmbeddedId
    private DepId depId;

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
        if(customers_Inside.size() == depCapability)
            return true;
        else
            return false;
    }

    public void setDepCapability(int maxCapability){
        this.depCapability = maxCapability;
    }

    public List<Customer> getCustomers_Inside() {
        return customers_Inside;
    }

    public int getMaxCapability() {
        return depCapability;
    }

    public DepId getId() {
        return depId;
    }

    public void setDepType(DepId depID){
        depId = depID;
    }
}
