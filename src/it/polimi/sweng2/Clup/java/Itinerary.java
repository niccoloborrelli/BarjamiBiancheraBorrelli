package it.polimi.sweng2.Clup.java;

import java.util.ArrayList;

public class Itinerary {
    private Store store;
    private ArrayList<Department> departments;
    private int visitDuration;

    public Itinerary(Store store, ArrayList<Department> departments, int visitDuration) {
        this.store = store;
        this.departments = departments;
        this.visitDuration = visitDuration;
    }

    public Store getStore() {
        return store;
    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }

    public int getVisitDuration() {
        return visitDuration;
    }
}
