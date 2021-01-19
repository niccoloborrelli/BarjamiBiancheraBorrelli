package it.polimi.sweng2.Clup.java.reservationManager;

import it.polimi.sweng2.Clup.java.Reservation;
import it.polimi.sweng2.Clup.java.Store;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@Stateful
public class ReservationManager implements StoreManagerServiceInt{

    EntityManager em;
    List<Reservation> lineUpList;
    List<Reservation> bookingList;
    Store store;

    public ReservationManager(Store store, EntityManager em) {
        this.store = store;
        this.em = em;
        lineUpList = new ArrayList<>();
        bookingList = new ArrayList<>();
    }

    @Override
    public int showNLineUp() {
        return lineUpList.size();
    }

    @Override
    public int showNBooking() {
        return bookingList.size();
    }

    @Override
    public int showNInside() {
        return store.getCustomers_Inside().size();
    }

}
