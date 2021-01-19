package it.polimi.sweng2.Clup.java.accountManager;

import it.polimi.sweng2.Clup.java.Reservation;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface CustomerProfile {

    List<Reservation> showCurrentReservation();
    void addReservation(Reservation r);
    void deleteReservation();
}
