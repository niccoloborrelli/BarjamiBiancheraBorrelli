package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import static java.lang.Math.toIntExact;

@Component
public class AccountManager implements CustomerProfile{

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    StoreRepository storeRepository;

    @Override
    public List<Reservation> showCurrentReservation(String email) {
        Customer c = customerRepository.findByEmail(email);
        return reservationRepository.findByUserIDAndStatus(c.getUserID(), "InTheQueue");
    }


    public void updateProfile(Reservation reservation){

        Customer c = customerRepository.findByUserID(reservation.getUserID());
        Store s = storeRepository.findById(reservation.getStoreID());
        if(c!=null && s!=null){
            //Update of mean of the store
            long totMeanStore = s.getAverageVisitDuration() * s.getNumberOfVisit();
            long realDuration = reservation.getExitTime() - reservation.getEntryTime();
            s.setNumberOfVisit(s.getNumberOfVisit() + 1);
            s.setAverageVisitDuration((totMeanStore+realDuration)/s.getNumberOfVisit());

            //update of mean of customer and delay
            long totDelay = c.getDelay() * c.getNumberOfVisit();
            long totMeanCustomer = c.getMeanVisitDuration() * c.getNumberOfVisit();
            c.setNumberOfVisit(c.getNumberOfVisit() + 1);
            c.setMeanVisitDuration((totMeanCustomer+realDuration)/c.getNumberOfVisit());
            c.setDelay((totDelay-reservation.getDuration())/c.getNumberOfVisit());

            storeRepository.save(s);
            customerRepository.save(c);
        }
    }

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }
}
