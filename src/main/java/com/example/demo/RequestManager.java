package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TimeZone;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//REMEMBER: ADD AN HOUR FOR EVERY VALUE EXTRACTED AND USED IN THE DB (e.g. if I have to use only user.delay, it must be added
//an hour to the result)
public class RequestManager implements CustomerServiceInt, Serializable {

    private static long hour = 3600000;

    @Autowired
    private ReservationManagerComponent reservationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Reservation reservation;
    public RequestManager(){
    }

    @Override
    public boolean submitRequest(RequestMessage requestMessage){
        if (requestMessage.getType().equals("lineUp_Request")) {
            LineUp_Request lineUp_request = createLineUpRequest(requestMessage);
            return submitLineUp(lineUp_request);
        } else if (requestMessage.getType().equals("booking_Request")) {
            BookingRequest bookingRequest = createBookingRequest(requestMessage);
            return submitBooking(bookingRequest);
        } else
            return false;
    }

    private LineUp_Request createLineUpRequest(RequestMessage requestMessage){
        Store s = storeRepository.findByChainAndAddress(requestMessage.getChain(), requestMessage.getAddress());
        System.out.println(s);
        LineUp_Request lineUp_request = new LineUp_Request();

        lineUp_request.setDate(Date.valueOf(LocalDate.now()));
        lineUp_request.setStartingTime(Time.valueOf(requestMessage.getStartingTime()).getTime() + hour);
        User u = userRepository.findByEmail(requestMessage.getUsername());
        System.out.println(requestMessage);
        System.out.println(requestMessage.username);
        System.out.println(u.getUserID());
        lineUp_request.setUserID(u.getUserID());
        lineUp_request.setStoreID(s.getId());
        lineUp_request.setResType("lineUp_Request");
        lineUp_request.setTimeOfInsertion(Time.valueOf(LocalTime.now()).getTime() + hour);
        lineUp_request.setCode((lineUp_request.getUserID() + LocalTime.now().toString()).hashCode());

        long newDuration;
        if(requestMessage.getDuration().equals("00:00:00") && u.getMeanVisitDuration()==0){
            lineUp_request.setDuration(s.getAverageVisitDuration());
        }else if(!requestMessage.getDuration().equals("00:00:00")) {
            newDuration = Time.valueOf(requestMessage.getDuration()).getTime() + u.getDelay() + hour;
            lineUp_request.setDuration(newDuration);
        }else{
            newDuration = u.getMeanVisitDuration() + u.getDelay();
            lineUp_request.setDuration(newDuration);
        }

        return lineUp_request;
    }

    private BookingRequest createBookingRequest(RequestMessage requestMessage){
        Store s = storeRepository.findByChainAndAddress(requestMessage.getChain(), requestMessage.getAddress());
        BookingRequest bookingRequest = new BookingRequest();

        bookingRequest.setDate(Date.valueOf(requestMessage.getData()));
        bookingRequest.setStartingTime(Time.valueOf(requestMessage.getStartingTime()).getTime() + hour);
        User u = userRepository.findByEmail(requestMessage.getUsername());
        bookingRequest.setUserID(u.getUserID());
        bookingRequest.setStoreID(s.getId());
        bookingRequest.setResType("booking_Request");
        bookingRequest.setTimeOfInsertion(Time.valueOf(LocalTime.now()).getTime()+hour);
        bookingRequest.setCode((bookingRequest.getUserID() + LocalTime.now().toString()).hashCode());

        long newDuration;
        if(requestMessage.getDuration().equals("00:00:00") && u.getMeanVisitDuration()==0){
            bookingRequest.setDuration(s.getAverageVisitDuration());
        }else if(!requestMessage.getDuration().equals("00:00:00")) {
            newDuration = Time.valueOf(requestMessage.getDuration()).getTime() + u.getDelay() + hour;
            bookingRequest.setDuration(newDuration);
        }else {
            newDuration = u.getMeanVisitDuration() + u.getDelay();
            bookingRequest.setDuration(newDuration);
        }

        return bookingRequest;
    }

    private boolean submitLineUp(LineUp_Request lineUp_request){
        if(!reservationManager.addRequest(lineUp_request)){
            System.out.println("False");
            reservation = lineUp_request;
            return false;
        }
        System.out.println("L'entry time dell'utente n " + lineUp_request.getUserID() + " con codice " + lineUp_request.getCode() +
                " entra alle " +new Time(lineUp_request.getEntryTime()-hour) + " ed esce alle " +new Time(lineUp_request.getExitTime()-hour));
        return true;
    }

    private boolean submitBooking(BookingRequest bookingRequest) {
        if(!reservationManager.addRequest(bookingRequest)){
            System.out.println("False");
            reservation = bookingRequest;
            return false;
        }
        System.out.println("L'entry time dell'utente n " + bookingRequest.getUserID() + " con codice " + bookingRequest.getCode() +
                " entra alle " +new Time(bookingRequest.getEntryTime()-hour) + " ed esce alle " +new Time(bookingRequest.getExitTime()-hour));
        return true;
    }

    @Override
    public boolean deleteRequest(DeleteMessage requestMessage) {
        Store s = storeRepository.findByChainAndAddress(requestMessage.getChain(), requestMessage.getAddress());
        Reservation reservation = reservationRepository.findByCodeAndStoreID(Integer.parseInt(requestMessage.getCode()), s.getId());
        return reservationManager.removeRequest(reservation);
    }

    public void setReservationManager(ReservationManagerComponent reservationManager) {
        this.reservationManager = reservationManager;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
}
