package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class ReservationManagerComponent implements StoreManagerServiceInt {

    ReservationManager reservationManager;

    @Autowired
    LineUpRepository lineUpRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreRepository storeRepository;

    @Override
    public int showNLineUp(int storeID) {
        createReservationManager(storeID, Date.valueOf(LocalDate.now()));
        return reservationManager.getLineUpList().size();
    }

    @Override
    public int showNBooking(int storeID) {
        createReservationManager(storeID, Date.valueOf(LocalDate.now()));
        return reservationManager.getBookingList().size();
    }

    @Override
    public int showNInside(int storeID) {
        createReservationManager(storeID, Date.valueOf(LocalDate.now()));
        return reservationManager.getStore().getCustomers_Inside().size();
    }

    private void createReservationManager(int storeID, Date date){
        Store s = storeRepository.findById(storeID);
        if(reservationManager == null) {
            reservationManager = new ReservationManager();
            reservationManager.setStore(s);
            reservationManager.setDate(date);
            reservationManager.setReservationRepository(reservationRepository);
            reservationManager.setUserRepository(userRepository);
            reservationManager.setStoreRepository(storeRepository);
            reservationManager.loadLists(storeID, date);
        }
        if(reservationManager.getStore().getId() != storeID || reservationManager.getDate().equals(date)) {
            reservationManager.loadLists(storeID, date);
            reservationManager.setStore(s);
            reservationManager.setDate(date);
        }
    }

    public synchronized boolean addRequest(LineUp_Request lineUp_request){
        createReservationManager(lineUp_request.getStoreID(), lineUp_request.getDate());
        if(reservationManager.verifyAvailability(lineUp_request)) {
            reservationManager.addRequest(lineUp_request);
            return true;
        }
        return false;
    }

    public synchronized boolean addRequest(BookingRequest bookingRequest){
        createReservationManager(bookingRequest.getStoreID(), bookingRequest.getDate());
        if(reservationManager.verifyAvailability(bookingRequest)) {
            reservationManager.addRequest(bookingRequest);
            return true;
        }
        return false;
    }

    public synchronized boolean removeRequest(Reservation reservation){
        createReservationManager(reservation.getStoreID(), reservation.getDate());
        return reservationManager.removeRequest(reservation);
    }

    public synchronized Reservation scan(int code, int storeID){
        createReservationManager(storeID, Date.valueOf(LocalDate.now()));
        return reservationManager.scan(code);
    }

    public void setLineUpRepository(LineUpRepository lineUpRepository) {
        this.lineUpRepository = lineUpRepository;
    }

    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }
}
