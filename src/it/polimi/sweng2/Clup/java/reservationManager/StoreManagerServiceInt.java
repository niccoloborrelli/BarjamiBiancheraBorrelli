package it.polimi.sweng2.Clup.java.reservationManager;

import javax.ejb.Remote;

@Remote
public interface StoreManagerServiceInt {

    int showNLineUp();
    int showNBooking();
    int showNInside();

}
