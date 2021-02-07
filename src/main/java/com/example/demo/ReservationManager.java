package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/*
Assumptions:
- Both lists are ordered by entry time
-Starting-time is also in line up request and indicates initially the time in which the customer can reach the store
- StartingTime in Booking indicates the entry Time, it will be registered in Entrytime when the reservation is inserted
- Before the insertion, the verifyAvailability is needed, the components won't control anyrhing during the add
- the possibility to set the zone by the store manager could be inserted
 */


public class ReservationManager{

//IT MUST BE VERIFIED IF A TOLERANCE OF DELAY IS NEEDED TO AVOID TO MANY DELAYS IN THE QUEUE

    private static int startPosition = 0;
    private static long hour = 3600000;
    private static final long minute = 60000;
    private List<Reservation> lineUpList;
    private List<Reservation> bookingList;
    private List<Reservation> handoutList;
    private Store store;
    private Date date;
    private HashMap<Integer, Timer> timersEntrance;
    private HashMap<Integer, Timer> timersDelay;


    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private StoreRepository storeRepository;

    public ReservationManager() {
        lineUpList = new ArrayList<>();
        bookingList = new ArrayList<>();
        handoutList = new ArrayList<>();
        timersEntrance = new HashMap<>();
        timersDelay = new HashMap<>();
    }

    /**
     * Loads the specific list for the date and for the store chosen
     * @param storeID is the id of the store chosen
     * @param date is the date chosen
     */

    public void loadLists(int storeID, Date date){
        lineUpList = reservationRepository.findByDateAndStoreIDAndResTypeAndStatus(date, storeID, "lineUp_Request", "InTheQueue");
        if(lineUpList==null)
            lineUpList = new ArrayList<>();

        lineUpList.sort(orderByEntryTime());

        bookingList = reservationRepository.findByDateAndStoreIDAndResTypeAndStatus(date, storeID, "booking_Request", "InTheQueue");

        if(bookingList == null)
            bookingList = new ArrayList<>();

        bookingList.sort(orderByEntryTime());

        this.date = date;

        this.store = storeRepository.findById(storeID);

        List<Reservation> reservationList = reservationRepository.findByDateAndStoreIDAndStatus(date, storeID, "Accepted");
        if(reservationList==null)
            this.store.setCustomers_Inside(new ArrayList<>());
        else
            this.store.setCustomers_Inside(new ArrayList<>(reservationList));

    }

    /**
     * Finds the entry time for the request, loads it in the database and adds the request to the list
     * @param lineUp_request is the request
     */

    public void addRequest(LineUp_Request lineUp_request){
        long newEntryTime = findBeginQueueTime(lineUp_request);
        if(newEntryTime==0) {
            newEntryTime = findEntryTimeRequest(lineUp_request);
            if (newEntryTime == 0) {
                newEntryTime = findLastEntryTime();
                if (newEntryTime == 0 || newEntryTime <= lineUp_request.getStartingTime())
                    newEntryTime = lineUp_request.getStartingTime();
            }
        }
        lineUp_request.setEntryTime(newEntryTime);
        lineUp_request.setExitTime(newEntryTime + lineUp_request.getDuration());
        lineUp_request.setStatus("InTheQueue");
        reservationRepository.save(lineUp_request);

        lineUpList.add(lineUp_request);
        lineUpList.sort(orderByEntryTime());


        boolean isHandout = isItHandout(lineUp_request);
        if(isHandout)
            handoutList.add(lineUp_request);

        if(ifSetNewFirstTime(lineUp_request))
            setNewFirstTimeTask(lineUp_request);


    }

    /**
     * Finds if the new reservation is the one that will enter first
     * @param reservation is the reservation analyzed
     * @return true if a it is, otherwise false
     */

    private boolean ifSetNewFirstTime(Reservation reservation) {
        List<Reservation> newList = new ArrayList<>(lineUpList);
        newList.addAll(bookingList);
        newList.sort(orderByEntryTime());
        if (newList.get(0).equals(reservation))
            return true;
        return false;
    }

    /**
     * Finds the entry time analyzing the possible insertion in the queue and in the beginning
     * @param lineUp_request is the request
     * @return the time found, null if there's not
     */


    private long findEntryTimeRequest(LineUp_Request lineUp_request){
        List<Reservation> exitTimeList = new ArrayList<>();
        exitTimeList.addAll(lineUpList);
        exitTimeList.addAll(bookingList);
        exitTimeList.addAll(store.getCustomers_Inside());
        List<Reservation> entranceTimeList = new ArrayList<>(exitTimeList);
        exitTimeList.sort(orderByExitTime());
        entranceTimeList.sort(orderByEntryTime());

        if(entranceTimeList.size()>=store.getMaxCapability()){
            List<Reservation> cutList = entranceTimeList.subList(store.getMaxCapability(),entranceTimeList.size());
            for(int i=0; i<cutList.size();i+=1) {
                Reservation exit = exitTimeList.get(i);
                Reservation entrance = cutList.get(i);
                if (exit.getExitTime() >= lineUp_request.getStartingTime()) {
                    if (entrance.getEntryTime() - exit.getExitTime() >= lineUp_request.getDuration()) {
                        return exit.getExitTime();
                    }
                } else {
                    if (entrance.getEntryTime() - lineUp_request.getStartingTime() >= lineUp_request.getDuration()) {
                        return lineUp_request.getStartingTime();
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Finds the time in case of the begin of the queue
     * @param r is the reservation that needs the entry time
     * @return the entry time if it exists, null otherwise
     */

    private long findBeginQueueTime(Reservation r){
        List<Reservation> entranceTimeList = new ArrayList<>();
        entranceTimeList.addAll(lineUpList);
        entranceTimeList.addAll(bookingList);
        entranceTimeList.addAll(store.getCustomers_Inside());
        entranceTimeList.sort(orderByEntryTime());
        if(entranceTimeList.size()<store.getMaxCapability()){
            return r.getStartingTime();
        }else if(entranceTimeList.get(store.getMaxCapability()-1).getEntryTime() >= r.getStartingTime() + r.getDuration()) {
            return r.getStartingTime();
        }
        return 0;
    }

    /**
     * Finds the entry time in the end of the queue
     * @return the entry time in the end of the queue
     */

    private long findLastEntryTime(){
        List<Reservation> exitTimeList = new ArrayList<>();
        exitTimeList.addAll(lineUpList);
        exitTimeList.addAll(bookingList);
        exitTimeList.addAll(store.getCustomers_Inside());
        exitTimeList.sort(orderByExitTime());

        if(exitTimeList.size()>=store.getMaxCapability())
            return exitTimeList.get(exitTimeList.size() - store.getMaxCapability()).getExitTime();
        else
            return 0;
    }

    /**
     * Creates new timer task for the entrance and the delay
     * @param r is the new first reservation
     */

    private void setNewFirstTimeTask(Reservation r){
        if(timersEntrance.get(r.getStoreID())==null)
            timersEntrance.put(r.getStoreID(), new Timer());

        Timer timerEntrance = timersEntrance.get(r.getStoreID());
        timerEntrance.cancel();
        timerEntrance.purge();
        timerEntrance = new Timer();
        TimerTask entrance = createEntranceTimerTask(r);
        if(r.getEntryTime()- Time.valueOf(LocalTime.now()).getTime() - hour >= 0) {
            System.out.println("The next user that has to enter is: " + r.getUserID());
            timerEntrance.schedule(entrance, r.getEntryTime() - Time.valueOf(LocalTime.now()).getTime() - hour);
        }else {
            System.out.println("The next user that has to enter is: " + r.getUserID());
            timerEntrance.schedule(entrance, 0);
        }
    }

    /**
     * Creates timer task for the entrance of the reservation r
     * @param r is the reservation that has to enter
     * @return the timer task created
     */

    private TimerTask createEntranceTimerTask(Reservation r){
        return new TimerTask() {
            final Reservation reservation = r;
            @Override
            public void run() {
                if(store.getCustomers_Inside().size()<store.getMaxCapability()) {
                    System.out.println("Time to enter for reservation with code: "+r.getCode());
                    TimerTask delay = createDelayTimeTask(reservation);
                    if(timersDelay.get(r.getStoreID())==null)
                        timersDelay.put(r.getStoreID(), new Timer());
                    Timer timerDelay = timersDelay.get(r.getStoreID());
                    timerDelay.schedule(delay, 300000);
                    //TO DO METHOD FOR NOTIFICATION
                }else {
                    System.out.println("There's a delay");
                    //TO DO METHOD FOR NOTIFICATION OF DELAY
                    //No timer set because of a scan in exit is needed

                }
            }
        };
    }

    /**
     * Creates the timer task for the delay of the reservation r
     * @param r is the reservation told above
     * @return the timer task for the delay
     */

    private TimerTask createDelayTimeTask(Reservation r){
        return new TimerTask() {
            final Reservation reservation = r;
            @Override
            public void run() {
                System.out.println("Time is over");
                if(!store.getCustomers_Inside().contains(reservation)){
                    reservation.setStatus("Rejected");
                    reservationRepository.save(reservation);
                    lineUpList.remove(reservation);
                    bookingList.remove(reservation);
                    findFirstHandout(r);
                }

            }
        };
    }

    /**
     * Finds if a handout could enter
     * @param reservation is the reservation removed
     */

    private void findFirstHandout(Reservation reservation){
        for(Reservation r: handoutList){
            lineUpList.remove(r);
            handoutList.remove(r);
            addRequest((LineUp_Request) r);
        }
    }

    /**
     * Adds the booking request to the db and the queue
     * @param bookingRequest is the booking request added
     */

    public void addRequest(BookingRequest bookingRequest){
        bookingRequest.setEntryTime(bookingRequest.getStartingTime());
        bookingRequest.setExitTime(bookingRequest.getStartingTime()+bookingRequest.getDuration());
        bookingRequest.setStatus("InTheQueue");
        reservationRepository.save(bookingRequest);
        bookingList.add(bookingRequest);
        bookingList.sort(orderByEntryTime());

        if(ifSetNewFirstTime(bookingRequest))
            setNewFirstTimeTask(bookingRequest);
    }

    /**
     * Verify availability for a lineUp reservation
     * @param r is the reservation to be analyzed
     * @return true if it could be accepted, false otherwise
     */

    public boolean verifyAvailability(LineUp_Request r) {
        return verifyAvailabilityLineUpRequest(r);
    }

    /**
     * Verify availability for a booking reservation
     * @param r is the reservation to be analyzed
     * @return true if it could be accepted, false otherwise
     */

    public boolean verifyAvailability(BookingRequest r) {
            return verifyAvailabilityBooking(r);
    }

    /**
     * Verify availability for a hand out - reservation
     * @param r is the reservation to be analyzed
     * @return true if it could be accepted, false otherwise
     */

    private boolean verifyAvailabilityHandOutRequest(LineUp_Request r){
        if(r.getStartingTime() + r.getDuration() <= store.getClosureTime() && r.getStartingTime()>= Time.valueOf(LocalTime.now()).getTime()+hour) {
            if (verifySimpleLineUpAvailability(r)) {
                return true;
            } else {
                return verifyPossibleInsertionInQueue(r);
            }
        }else
            return false;

    }

    /**
     * Verify availability for a lineUp reservation
     * @param r is the reservation to be analyzed
     * @return true if it could be accepted, false otherwise
     */
    private boolean verifyAvailabilityLineUpRequest(LineUp_Request r) {
        List<Integer> idInQueue = lineUpList.stream().map(Reservation::getUserID).collect(Collectors.toList());
        if(idInQueue.contains(r.getUserID())) {
            System.out.println("He/she can't do multiple lineUps in the same store");
            return false;
        }
        return verifyAvailabilityHandOutRequest(r);
    }

    /**
     * Verifies if there are spaces in the end of the queue or in the beginning
     * @param r is the reservation analyzed
     * @return true if it could be accepted, false otherwise
     */

    //this method verifies if there's spaces in the beginning or end of the queue (not in the middle)
    private boolean verifySimpleLineUpAvailability(LineUp_Request r) {
        List<Reservation> temporaryList = new ArrayList<>();

        temporaryList.addAll(lineUpList);
        temporaryList.addAll(bookingList);
        temporaryList.addAll(store.getCustomers_Inside());
        temporaryList.sort(orderByExitTime());

        if (store.getMaxCapability() - store.getCustomers_Inside().size() == 0) {
                verifyEndQueue(r, temporaryList);
        }else{
                if (!verifyEndQueue(r, temporaryList))
                    return verifyInsertionBeginQueue(r);
                else{
                    return true;
                }
        }
        return false;
    }

    /**
     * Verifies if there are spaces in the end of the queue or in the beginning
     * @param r is the reservation analyzed
     * @param temporaryList is the list that contains all the reservation
     * @return true if it could be accepted, false otherwise
     */

    private boolean verifyEndQueue(LineUp_Request r, List<Reservation> temporaryList){
        Reservation associated;
        if(temporaryList.size() >= store.getMaxCapability()) {
            associated = temporaryList.get(temporaryList.size() - store.getMaxCapability());
            //check if there is space before the closure
            if (associated.getExitTime() > r.getStartingTime()) { //if the customer will arrive before t is exited
                return associated.getExitTime() + r.getDuration() <= store.getClosureTime();
            } else {
                return r.getStartingTime() + r.getDuration() <= store.getClosureTime();
            }
        }else{
            return true;
        }
    }

    /**
     * Verifies if there's a space in the beginning of the queue
     * @param r is the reservation analyzed
     * @return true if it could be accepted, false otherwise
     */

    private boolean verifyInsertionBeginQueue(Reservation r){
        List<Reservation> entranceTimeList = new ArrayList<>();
        entranceTimeList.addAll(lineUpList);
        entranceTimeList.addAll(bookingList);
        entranceTimeList.addAll(store.getCustomers_Inside());
        entranceTimeList.sort(orderByEntryTime());
        if(entranceTimeList.size()<store.getMaxCapability()){
            return true;
        }else if(entranceTimeList.get(store.getMaxCapability()-1).getEntryTime() >= r.getStartingTime() + r.getDuration()) {
            return true;
        }
        return false;
    }

    /**
     * Verifies if there's a space between the reservations accepted
     * @param lineUp_request is the request analyzed
     * @return true if there's, otherwise false
     */

    private boolean verifyPossibleInsertionInQueue(LineUp_Request lineUp_request){
        List<Reservation> exitTimeList = new ArrayList<>();
        exitTimeList.addAll(lineUpList);
        exitTimeList.addAll(bookingList);
        exitTimeList.addAll(store.getCustomers_Inside());
        List<Reservation> entranceTimeList = new ArrayList<>(exitTimeList);
        exitTimeList.sort(orderByExitTime());
        entranceTimeList.sort(orderByEntryTime());

        if(entranceTimeList.size()>=store.getMaxCapability()){
            List<Reservation> cutList = entranceTimeList.subList(store.getMaxCapability(),entranceTimeList.size());
            for(int i=0; i<cutList.size();i+=1) {
                Reservation exit = exitTimeList.get(i);
                Reservation entrance = cutList.get(i);
                if (exit.getExitTime() >= lineUp_request.getStartingTime()) {
                    if (entrance.getEntryTime() - exit.getExitTime() >= lineUp_request.getDuration()) {
                        return true;
                    }
                } else {
                    if (entrance.getEntryTime() - lineUp_request.getStartingTime() >= lineUp_request.getDuration()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Verifies if a booking could be inserted in the queue
     * @param r is the reservation analyzed
     * @return true if there's, false otherwise
     */

    private boolean verifyAvailabilityBooking(BookingRequest r) {
        if(r.getStartingTime() + r.getDuration() <= store.getClosureTime()) {
            if(r.getDate().equals(Date.valueOf(LocalDate.now()))) {
                if (r.getStartingTime() <= Time.valueOf(LocalTime.now()).getTime() + hour)
                    return false;
            }
            List<Integer> idInQueue = bookingList.stream().map(Reservation::getUserID).collect(Collectors.toList());
            if(idInQueue.contains(r.getUserID())) {
                System.out.println("User can't do another booking in this store");
                return false;
            }
            if (!verifySimpleBookingAvailability(r, lineUpList, bookingList, true))
                return verifyPossibleHoleInQueue(r, lineUpList, bookingList, true);
            else {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if there's no other customer after this reservation
     * @param r is the reservation analyzed
     * @param lineUpList is the list that contains the lineUp requests already accepted
     * @param bookingList is the list that contains the booking requests already accepted
     * @param today if it's another day, or this day
     * @return true if it could be accepted, false otherwise
     */

    private boolean verifySimpleBookingAvailability(BookingRequest r, List<Reservation> lineUpList, List<Reservation> bookingList,
                                                    boolean today) {
        List<Reservation> temporaryList = new ArrayList<>();
        Reservation associated;

        temporaryList.addAll(lineUpList);
        temporaryList.addAll(bookingList);

        if(today)
            temporaryList.addAll(store.getCustomers_Inside());
        temporaryList.sort(orderByExitTime());

        if (temporaryList.size() - store.getMaxCapability() >= 0) {
            associated = temporaryList.get(temporaryList.size() - store.getMaxCapability());
            if (r.getStartingTime() >= associated.getExitTime())
                return true;
        }
        return verifyInsertionBeginQueue(r);
    }

    /**
     * Verifies if there's a possible hole in the queue for that specific time
     * @param r is the reservation analyzed
     * @param lineUpList is the list that contains the lineUp requests already accepted
     * @param bookingList is the list that contains the booking requests already accepted
     * @param today if it's another day, or this day
     * @return true if it could be accepted, false otherwise
     */
    private boolean verifyPossibleHoleInQueue(BookingRequest r, List<Reservation> lineUpList, List<Reservation> bookingList,
                                              boolean today){
        List<Reservation> exitTimeList = new ArrayList<>();
        exitTimeList.addAll(lineUpList);
        exitTimeList.addAll(bookingList);
        if(today)
            exitTimeList.addAll(store.getCustomers_Inside());
        List<Reservation> entranceTimeList = new ArrayList<>(exitTimeList);
        exitTimeList.sort(orderByExitTime());
        entranceTimeList.sort(orderByEntryTime());

        if(entranceTimeList.size()>=store.getMaxCapability()){
            List<Reservation> cutList = entranceTimeList.subList(store.getMaxCapability(),entranceTimeList.size());
            for(int i=0; i<cutList.size();i+=1) {
                Reservation exit = exitTimeList.get(i);
                Reservation entrance = cutList.get(i);
                if (exit.getExitTime() <= r.getStartingTime()) {
                    if (entrance.getEntryTime() - r.getStartingTime() >= r.getDuration()) {
                        return true;
                    }
                }
            }
        }
            return false;
    }

    /**
     * Scan the reservation
     * @param code is the code scanned
     * @return the reservation scanned, if it's not accepted null
     */

    public Reservation scan(int code){
        Reservation r= extrapolateFromList(code, lineUpList);
        if(r==null)
            r = extrapolateFromList(code, bookingList);
        if(r!=null)
            return scanEntrance(r);
        else {
            r = extrapolateFromList(code, store.getCustomers_Inside());
            if(r!=null)
                return scanLeaving(r);
            else
                return null;
        }
    }

    /**
     * Scan the entrance of a specific reservation
     * @param r is the reservation scanned
     * @return the reservation scanned, if it's not accepted null
     */

    private Reservation scanEntrance(Reservation r){
        System.out.println("Entrance scan");
        boolean isHandout = isItHandout(r);
        List<Reservation> temp = new ArrayList<>(lineUpList);
        temp.addAll(bookingList);
        temp.sort(orderByEntryTime());
        if(Time.valueOf(LocalTime.now()).getTime() + hour >=r.getEntryTime() && store.getMaxCapability()>store.getCustomers_Inside().size() ){
            List<Reservation> canEnter;
            if(store.getMaxCapability() - store.getCustomers_Inside().size() <= temp.size())
                canEnter = temp.subList(0,store.getMaxCapability()-store.getCustomers_Inside().size());
            else
                canEnter = temp.subList(0,temp.size());
            if(canEnter.contains(r)){
                lineUpList.remove(r);
                bookingList.remove(r);
                store.getCustomers_Inside().add(r);
                r.setStatus("Accepted");
                reservationRepository.save(r);
                temp.remove(r);
                if(isHandout)
                    handoutList.remove(r);

                if(temp.size()>0)
                    setNewFirstTimeTask(temp.get(0));


                return r;
            }
        }
        return null;
    }

    /**
     * Scan the reservation for the leaving
     * @param r is the reservation scanned
     * @return the reservation scanned if accepted, null otherwise
     */

    private Reservation scanLeaving(Reservation r){
        System.out.println("Leaving Scan");
        store.getCustomers_Inside().remove(r);
        r.setStatus("Exited");
        reservationRepository.save(r);

        if(store.getCustomers_Inside().size()+1==store.getMaxCapability()) {
            List<Reservation> reservationList = new ArrayList<>(lineUpList);
            reservationList.addAll(bookingList);
            reservationList.sort(orderByEntryTime());
            setNewFirstTimeTask(reservationList.get(0));
        }
        return r;
    }

    /**
     * Extrapolate the code from a list
     * @param code is the code of the reservation that has to be extracted
     * @param reservationList is the list containing all the reservaition
     * @return the reservation found, null otherwise
     */

    private Reservation extrapolateFromList(int code, List<Reservation> reservationList){
        for(Reservation r: reservationList)
            if(r.getCode()==code){
                return r;
            }
        return null;
    }


    /**
     * Order by exit time
     * @return the comparator
     */

    private Comparator<Reservation> orderByExitTime() {
        return new Comparator<Reservation>() {
            @Override
            public int compare(Reservation o1, Reservation o2) {
                return Long.compare(o1.getExitTime(), o2.getExitTime());
            }
        };
    }

    /**
     * Order by entry time
     * @return the comparator
     */

    private Comparator<Reservation> orderByEntryTime() {
        return new Comparator<Reservation>() {
            @Override
            public int compare(Reservation o1, Reservation o2) {
                return Long.compare(o1.getEntryTime(), o2.getEntryTime());
            }
        };
    }

    /**
     * Remove the request
     * @param request is the request removed
     * @return true if it could be removed, otherwise null
     */

    public boolean removeRequest(Reservation request){
        //Put 20 minutes instead of 1

        if(request.getEntryTime()-Time.valueOf(LocalTime.now()).getTime()-hour>=1*minute || request.getDate().after(Date.valueOf(LocalDate.now()))) { //Assumption of minutes

            List<Reservation> temporary = new ArrayList<>(lineUpList);
            temporary.addAll(bookingList);
            temporary.sort(orderByEntryTime());
            if (temporary.contains(request)) {
                request.setStatus("Rejected");
                reservationRepository.save(request);

                int indexRemoved = temporary.indexOf(request);
                List<Reservation> toBeFixed = temporary.subList(indexRemoved, temporary.size());
                lineUpList.remove(request);
                bookingList.remove(request);
                handoutList.remove(request);
                reOrderList(toBeFixed);
                return true;
            } else
                return false;
        }
        return false;
    }

    /**
     * Re order the list after a remove
     * @param toBeFixed is the list that has to be fixed
     */

    private void reOrderList(List<Reservation> toBeFixed){
        for(Reservation r: toBeFixed)
            if(r.getResType().equals("lineUp_Request")){
                if(r.getEntryTime()!=r.getStartingTime()) {
                    System.out.println("Time to re-order the list due to a remove");
                    if(Time.valueOf(LocalTime.now()).getTime()+ hour + (r.getStartingTime() -r.getTimeOfInsertion()) <= r.getEntryTime()) {
                        r.setStartingTime(Time.valueOf(LocalTime.now()).getTime() + hour + (r.getStartingTime() - r.getTimeOfInsertion()));
                        r.setTimeOfInsertion(Time.valueOf(LocalTime.now()).getTime()+hour);
                        System.out.println("New Starting time: " + new Time(Time.valueOf(LocalTime.now()).getTime() + (r.getStartingTime() - r.getTimeOfInsertion())) +
                                " of reservation with id: " + r.getId());
                        //reservationRepository.save(r);
                        lineUpList.remove(r);
                        handoutList.remove(r);
                        //loadLists(r.getStoreID(), r.getDate());
                        addRequest((LineUp_Request) r);
                    }
                    //reservationRepository.delete(r);
                }
            }
    }

    /**
     * Verifies if a user is an handout
     * @param r is the reservation analyzed
     * @return true if it, otherwise not
     */

    private boolean isItHandout(Reservation r){
        User u = userRepository.findByUserID(r.getUserID());
        if(u!=null) {
            if (u.getType().equals("handout"))
                return true;
        }
        return false;
    }

    public void setLineUpList(List<Reservation> lineUpList) {
        this.lineUpList = lineUpList;
    }

    public void setBookingList(List<Reservation> bookingList) {
        this.bookingList = bookingList;
    }


    public List<Reservation> getLineUpList() {
        return lineUpList;
    }

    public List<Reservation> getBookingList() {
        return bookingList;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Reservation> getHandoutList() {
        return handoutList;
    }

    public void setHandoutList(List<Reservation> handoutList) {
        this.handoutList = handoutList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ReservationRepository getReservationRepository() {
        return reservationRepository;
    }

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }
}


