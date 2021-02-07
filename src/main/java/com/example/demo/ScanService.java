package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScanService implements ScanClerkServiceInt {

    @Autowired
    private ReservationManagerComponent reservationManagerComponent;

    @Autowired
    private AccountManager accountManager;


    @Override
    public boolean scan(int code, int storeID) {
        Reservation r =  reservationManagerComponent.scan(code, storeID);
        if(r!= null && r.getStatus().equals("Exited")) {
            System.out.println("Profile update");
            accountManager.updateProfile(r);
        }
        return r != null;
    }
}
