package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class ReservationManagerTest{

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;
    /*
    VERIFY AVAILABILITY LINE UP TESTS SECTION
     */

    /*
    Test that verifies the availability in case a request occurs, no people in the queue and the probable exit time before closure
    Result expected: true
     */
    @Test
    void verifyAvailabilityLineUp1() {
        long hour = 3600000;
        long minute = 60000;
        LocalTime lt = LocalTime.now();
        long actual = Time.valueOf(lt).getTime() + hour;
        long closure = actual + hour;
        long aperture = closure - hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();


        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);
        reservationManager.loadLists(store.getId(), Date.valueOf(LocalDate.now()));


        LineUp_Request lineUpRequest = new LineUp_Request();

        lineUpRequest.setStartingTime(actual+5*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setDuration(20*minute);
        lineUpRequest.setUserID(u.getUserID());

        System.out.println(reservationManager.getLineUpList());
        System.out.println(reservationManager.getBookingList());
        assertTrue(reservationManager.verifyAvailability(lineUpRequest));
    }

    /*
    Test that verifies the non-availability in case of duration that goes beyond the closure time.
    Result expected: false
     */

    @Test
    void verifyAvailabilityLineUp2() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = Time.valueOf(lt).getTime()+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);


        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);
        reservationManager.loadLists(store.getId(), Date.valueOf(LocalDate.now()));

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual + 50*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setDuration(20*minute);
        assertFalse(reservationManager.verifyAvailability(lineUpRequest));
    }

    /*
    It verifies the availability if the queue isn't empty, but there's space at the end
     */
    @Test
    void verifyAvailabilityLineUp3() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure -hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);
        reservationManager.loadLists(store.getId(), Date.valueOf(LocalDate.now()));

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+50*minute);
        line1.setExitTime(closure);
        line1.setUserID(0);
        testEntityManager.persist(line1);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+50*minute);
        line2.setExitTime(actual+55*minute);
        line2.setUserID(1);
        testEntityManager.persist(line2);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+50*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setUserID(2);
        lineUpRequest.setDuration(4*minute);
        assertTrue(reservationManager.verifyAvailability(lineUpRequest));
    }


    /*
    It verifies the availability if the queue isn't empty, there's space at the end, but the duration is beyond the closure time
     */
    @Test
    void verifyAvailabilityLineUp4() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = Time.valueOf(lt).getTime()+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);
        reservationManager.loadLists(store.getId(), Date.valueOf(LocalDate.now()));

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+50*minute);
        line1.setExitTime(closure);
        line1.setUserID(0);
        testEntityManager.persist(line1);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+50*minute);
        line2.setExitTime(actual+55*minute);
        line2.setUserID(1);
        testEntityManager.persist(line2);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+50*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setUserID(2);
        lineUpRequest.setDuration(9*minute);
        assertFalse(reservationManager.verifyAvailability(lineUpRequest));
    }

    /*
    It verifies that a reservation can be accepted if there's a space in the beginning of the queue that doesn't modify
    the other entry time
     */

    @Test
    void verifyAvailabilityLineUp5() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);


        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);
        reservationManager.loadLists(store.getId(), Date.valueOf(LocalDate.now()));

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+50*minute);
        line1.setExitTime(closure);
        line1.setUserID(0);
        testEntityManager.persist(line1);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+50*minute);
        line2.setExitTime(closure);
        line2.setUserID(1);
        testEntityManager.persist(line2);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+40*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setUserID(2);
        lineUpRequest.setDuration(9*minute);
        assertTrue(reservationManager.verifyAvailability(lineUpRequest));
    }

    /*
    It verifies that if there's a space between other reservations, it returns true (Case startingTime< queue.ExitTime)
     */

    @Test
    void verifyAvailabilityLineUp6() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);


        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+30*minute);
        line1.setExitTime(actual+45*minute);
        line1.setUserID(0);
        testEntityManager.persist(line1);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+20*minute);
        line2.setExitTime(actual+45*minute);
        line2.setUserID(1);
        testEntityManager.persist(line2);

        LineUp_Request line3 = new LineUp_Request();
        line3.setEntryTime(actual+45*minute);
        line3.setExitTime(closure);
        line3.setUserID(2);
        testEntityManager.persist(line3);

        LineUp_Request line4 = new LineUp_Request();
        line4.setEntryTime(actual+55*minute);
        line4.setExitTime(closure);
        line4.setUserID(3);
        testEntityManager.persist(line4);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);
        reservationManager.getLineUpList().add(line3);
        reservationManager.getLineUpList().add(line4);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+40*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setUserID(4);
        lineUpRequest.setDuration(9*minute);
        assertTrue(reservationManager.verifyAvailability(lineUpRequest));
    }

        /*
    It verifies that if there's a space between other reservations, it returns true (Case startingTime>queue.ExitTime)
     */

    @Test
    void verifyAvailabilityLineUp7() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+30*minute);
        line1.setExitTime(actual+45*minute);
        line1.setUserID(0);
        testEntityManager.persist(line1);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+20*minute);
        line2.setExitTime(actual+45*minute);
        line2.setUserID(1);
        testEntityManager.persist(line2);

        LineUp_Request line3 = new LineUp_Request();
        line3.setEntryTime(actual+45*minute);
        line3.setExitTime(closure);
        line3.setUserID(2);
        testEntityManager.persist(line3);

        LineUp_Request line4 = new LineUp_Request();
        line4.setEntryTime(actual+50*minute);
        line4.setExitTime(actual+55*minute);
        line4.setUserID(3);
        testEntityManager.persist(line4);

        LineUp_Request line5 = new LineUp_Request();
        line5.setEntryTime(actual+58*minute);
        line5.setExitTime(closure);
        line5.setUserID(4);
        testEntityManager.persist(line5);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);
        reservationManager.getLineUpList().add(line3);
        reservationManager.getLineUpList().add(line4);
        reservationManager.getLineUpList().add(line5);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+56*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setUserID(5);
        lineUpRequest.setDuration(2*minute);
        assertTrue(reservationManager.verifyAvailability(lineUpRequest));
    }

    /*
    It verifies that if there's space in the queue, but the duration will change already accepted starting time, the request
    is rejected
     */

    @Test
    void verifyAvailabilityLineUp8() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+30*minute);
        line1.setExitTime(actual+45*minute);
        line1.setUserID(0);
        testEntityManager.persist(line1);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+20*minute);
        line2.setExitTime(actual+45*minute);
        line2.setUserID(1);
        testEntityManager.persist(line2);

        LineUp_Request line3 = new LineUp_Request();
        line3.setEntryTime(actual+45*minute);
        line3.setExitTime(closure);
        line3.setUserID(2);
        testEntityManager.persist(line3);

        LineUp_Request line4 = new LineUp_Request();
        line4.setEntryTime(actual+50*minute);
        line4.setExitTime(actual+55*minute);
        line4.setUserID(3);
        testEntityManager.persist(line4);

        LineUp_Request line5 = new LineUp_Request();
        line5.setEntryTime(actual+58*minute);
        line5.setExitTime(closure);
        line5.setUserID(4);
        testEntityManager.persist(line5);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);
        reservationManager.getLineUpList().add(line3);
        reservationManager.getLineUpList().add(line4);
        reservationManager.getLineUpList().add(line5);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+56*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setDuration(3*minute);
        assertFalse(reservationManager.verifyAvailability(lineUpRequest));
    }

    /*
    VERIFY AVAILABILITY BOOKING
     */
    /*
    It verifies that a booking is accepted with a empty store and queue.
     */
    @Test
    void verifyAvailabilityBooking1() {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();


        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStartingTime(actual+5*minute);
        bookingRequest.setCode(1);
        bookingRequest.setDate(Date.valueOf(ld));
        bookingRequest.setDuration(20*minute);
        assertTrue(reservationManager.verifyAvailability(bookingRequest));
    }
    /*
    It verifies that if the reservation ends beyond the closure time, it's rejected.
     */

    @Test
    void verifyAvailabilityBooking2() {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStartingTime(actual+50*minute);
        bookingRequest.setCode(1);
        bookingRequest.setDate(Date.valueOf(ld));
        bookingRequest.setDuration(20*minute);
        assertFalse(reservationManager.verifyAvailability(bookingRequest));
    }
    /*
    It verifies that if the time chosen isn't available, it's rejected
     */

    @Test
    void verifyAvailabilityBooking3() {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = Time.valueOf(lt).getTime()+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        BookingRequest booking1 = new BookingRequest();
        booking1.setEntryTime(actual+50*minute);
        booking1.setExitTime(closure);
        testEntityManager.persist(booking1);

        BookingRequest booking2 = new BookingRequest();
        booking2.setEntryTime(actual+50*minute);
        booking2.setExitTime(actual+55*minute);
        testEntityManager.persist(booking2);

        reservationManager.getLineUpList().add(booking1);
        reservationManager.getLineUpList().add(booking2);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setDate(Date.valueOf(ld));
        bookingRequest.setStartingTime(actual+50*minute);
        bookingRequest.setCode(1);
        bookingRequest.setDuration(4*minute);
        assertFalse(reservationManager.verifyAvailability(bookingRequest));
    }

    /*
    It verifies that it could be put before all reservation in queue
     */

    @Test
    void verifyAvailabilityBooking4() {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        BookingRequest booking1 = new BookingRequest();
        booking1.setEntryTime(actual+50*minute);
        booking1.setExitTime(closure);
        testEntityManager.persist(booking1);

        BookingRequest booking2 = new BookingRequest();
        booking2.setEntryTime(actual+50*minute);
        booking2.setExitTime(closure);
        testEntityManager.persist(booking2);

        reservationManager.getLineUpList().add(booking1);
        reservationManager.getLineUpList().add(booking2);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStartingTime(actual+40*minute);
        bookingRequest.setCode(1);
        bookingRequest.setDate(Date.valueOf(ld));
        bookingRequest.setDuration(9*minute);
        assertTrue(reservationManager.verifyAvailability(bookingRequest));
    }

    /*
    It verifies that if there's a space in the queue, but the time chosen isn't matched with it, the reservation is rejected
     */

    @Test
    void verifyAvailabilityBooking5() {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+30*minute);
        line1.setExitTime(actual+45*minute);
        testEntityManager.persist(line1);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+20*minute);
        line2.setExitTime(actual+45*minute);
        testEntityManager.persist(line2);

        LineUp_Request line3 = new LineUp_Request();
        line3.setEntryTime(actual+45*minute);
        line3.setExitTime(closure);
        testEntityManager.persist(line3);

        LineUp_Request line4 = new LineUp_Request();
        line4.setEntryTime(actual+55*minute);
        line4.setExitTime(closure);
        testEntityManager.persist(line4);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);
        reservationManager.getLineUpList().add(line3);
        reservationManager.getLineUpList().add(line4);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStartingTime(actual+40*minute);
        bookingRequest.setCode(1);
        bookingRequest.setDate(Date.valueOf(ld));
        bookingRequest.setDuration(9*minute);
        assertFalse(reservationManager.verifyAvailability(bookingRequest));
    }

    /*
    It verifies that if there's a space in the queue and it matches with the starting time, it's accepted
     */

    @Test
    void verifyAvailabilityBooking6() {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+30*minute);
        line1.setExitTime(actual+45*minute);
        testEntityManager.persist(line1);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+20*minute);
        line2.setExitTime(actual+45*minute);
        testEntityManager.persist(line2);

        LineUp_Request line3 = new LineUp_Request();
        line3.setEntryTime(actual+45*minute);
        line3.setExitTime(closure);
        testEntityManager.persist(line3);

        LineUp_Request line4 = new LineUp_Request();
        line4.setEntryTime(actual+55*minute);
        line4.setExitTime(closure);
        testEntityManager.persist(line4);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);
        reservationManager.getLineUpList().add(line3);
        reservationManager.getLineUpList().add(line4);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStartingTime(actual+46*minute);
        bookingRequest.setCode(1);
        bookingRequest.setDate(Date.valueOf(ld));
        bookingRequest.setDuration(9*minute);
        assertTrue(reservationManager.verifyAvailability(bookingRequest));
    }

    /*
    There's a space, but the duration will change an other entry time, so it's rejected
     */

    @Test
    void verifyAvailabilityBooking7() {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        Customer u = new Customer();
        u.setEmail("cicciogamer89@youtube.it");
        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        testEntityManager.persist(u);
        testEntityManager.flush();
        testEntityManager.persist(store);
        testEntityManager.flush();

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+30*minute);
        line1.setExitTime(actual+45*minute);
        testEntityManager.persist(line1);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+20*minute);
        line2.setExitTime(actual+45*minute);
        testEntityManager.persist(line2);

        LineUp_Request line3 = new LineUp_Request();
        line3.setEntryTime(actual+45*minute);
        line3.setExitTime(closure);
        testEntityManager.persist(line3);

        LineUp_Request line4 = new LineUp_Request();
        line4.setEntryTime(actual+50*minute);
        line4.setExitTime(actual+55*minute);
        testEntityManager.persist(line4);

        LineUp_Request line5 = new LineUp_Request();
        line5.setEntryTime(actual+58*minute);
        line5.setExitTime(closure);
        testEntityManager.persist(line5);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);
        reservationManager.getLineUpList().add(line3);
        reservationManager.getLineUpList().add(line4);
        reservationManager.getLineUpList().add(line5);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStartingTime(actual+56*minute);
        bookingRequest.setCode(1);
        bookingRequest.setDate(Date.valueOf(ld));
        bookingRequest.setDuration(3*minute);
        assertFalse(reservationManager.verifyAvailability(bookingRequest));
    }

    /*
    ADDING REQUEST TEST SECTION
     */

    /*
    It checks if the entry time is equal to the starting time in case of store and queue empty.
     */
    @Test
    void addLineUp1() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime()+hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setDuration(20*minute);
        reservationManager.addRequest(lineUpRequest);
        assertEquals(lineUpRequest.getEntryTime(), actual+minute);
    }

    /*
    It verifies if the entry time is slipped in case of a reservation that exits after its starting time. (end of queue)
     */

    @Test
    void addLineUp2() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+50*minute);
        line1.setExitTime(closure);
        line1.setUserID(0);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+50*minute);
        line2.setExitTime(actual+55*minute);
        line2.setUserID(1);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+50*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setUserID(2);
        lineUpRequest.setDuration(4*minute);
        reservationManager.addRequest(lineUpRequest);
        assertEquals(lineUpRequest.getEntryTime(), actual + 55*minute);
    }
    /*
    It verifies if the entry time is correct in case of insertion in the begin of the queue
     */

    @Test
    void addLineUp3() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+30*minute);
        line1.setExitTime(closure);
        line1.setUserID(0);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+50*minute);
        line2.setExitTime(closure);
        line2.setUserID(1);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+40*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setUserID(2);
        lineUpRequest.setDuration(9*minute);
        reservationManager.addRequest(lineUpRequest);
        assertEquals(lineUpRequest.getEntryTime(),actual+40*minute);
    }

    /*
    It verifies that if there's a space between other reservations, it returns true (Case startingTime< queue.ExitTime)
     */

    @Test
    void addLineUp4() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+30*minute);
        line1.setExitTime(actual+45*minute);
        line1.setUserID(0);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+20*minute);
        line2.setExitTime(actual+45*minute);
        line2.setUserID(1);

        LineUp_Request line3 = new LineUp_Request();
        line3.setEntryTime(actual+45*minute);
        line3.setExitTime(closure);
        line3.setUserID(2);

        LineUp_Request line4 = new LineUp_Request();
        line4.setEntryTime(actual+55*minute);
        line4.setExitTime(closure);
        line4.setUserID(3);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);
        reservationManager.getLineUpList().add(line3);
        reservationManager.getLineUpList().add(line4);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+40*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setUserID(4);
        lineUpRequest.setDuration(9*minute);
        reservationManager.addRequest(lineUpRequest);
        assertEquals(lineUpRequest.getEntryTime(), actual+45*minute);
    }

    /*
    It verifies that if there's a space between other reservations, it returns true (Case startingTime>queue.ExitTime)
     */

    @Test
    void addLineUp5() {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = Time.valueOf(lt).getTime()+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);

        LineUp_Request line1 = new LineUp_Request();
        line1.setEntryTime(actual+30*minute);
        line1.setExitTime(actual+45*minute);
        line1.setUserID(0);

        LineUp_Request line2 = new LineUp_Request();
        line2.setEntryTime(actual+20*minute);
        line2.setExitTime(actual+45*minute);
        line2.setUserID(1);

        LineUp_Request line3 = new LineUp_Request();
        line3.setEntryTime(actual+45*minute);
        line3.setExitTime(closure);
        line3.setUserID(2);

        LineUp_Request line4 = new LineUp_Request();
        line4.setEntryTime(actual+50*minute);
        line4.setExitTime(actual+55*minute);
        line4.setUserID(3);

        LineUp_Request line5 = new LineUp_Request();
        line5.setEntryTime(actual+58*minute);
        line5.setExitTime(closure);
        line5.setUserID(4);

        reservationManager.getLineUpList().add(line1);
        reservationManager.getLineUpList().add(line2);
        reservationManager.getLineUpList().add(line3);
        reservationManager.getLineUpList().add(line4);
        reservationManager.getLineUpList().add(line5);

        LineUp_Request lineUpRequest = new LineUp_Request();
        lineUpRequest.setStartingTime(actual+56*minute);
        lineUpRequest.setCode(1);
        lineUpRequest.setUserID(5);
        lineUpRequest.setDuration(2*minute);
        reservationManager.addRequest(lineUpRequest);
        assertEquals(lineUpRequest.getEntryTime(), actual+56*minute);
    }

    @Test
    void addBooking(){
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStartingTime(actual+3*minute);
        bookingRequest.setDuration(3*minute);

        reservationManager.addRequest(bookingRequest);
        assertEquals(bookingRequest.getEntryTime(), actual+3*minute);
    }

    /*
    Simulation of situation with scan in entrance and leaving (no delays)
     */

    @Test
    void complexSituation1() throws InterruptedException {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(closure-hour);
        storeRepository.save(store);

        testEntityManager.persist(store);
        testEntityManager.flush();

        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);

        System.out.println(store.getId());
        System.out.println(testEntityManager.find(Store.class, store.getId()));
        System.out.println(storeRepository.findById(store.getId()));
        reservationManager.loadLists(store.getId(), Date.valueOf(LocalDate.now()));

        LineUp_Request lineUp1 = createLineUp(0, aperture+minute, minute);
        LineUp_Request lineUp2 = createLineUp(1, aperture+minute*2, minute);
        LineUp_Request lineUp3 = createLineUp(2, aperture+minute, minute);
        LineUp_Request lineUp4 = createLineUp(3, aperture+minute*3, minute);
        LineUp_Request lineUp5 = createLineUp(4, aperture+minute*4, minute);
        LineUp_Request lineUp6 = createLineUp(5, aperture+minute*5, minute);
        List<LineUp_Request> list = new ArrayList<>();
        lineUp1.setUserID(0);
        lineUp2.setUserID(1);
        lineUp3.setUserID(2);
        lineUp4.setUserID(3);
        lineUp5.setUserID(4);
        lineUp6.setUserID(5);

        lineUp1.setStoreID(store.getId());
        lineUp2.setStoreID(store.getId());
        lineUp3.setStoreID(store.getId());
        lineUp4.setStoreID(store.getId());
        lineUp5.setStoreID(store.getId());
        lineUp6.setStoreID(store.getId());

        list.add(lineUp1);
        list.add(lineUp2);
        list.add(lineUp3);
        list.add(lineUp4);
        list.add(lineUp5);
        list.add(lineUp6);

        for(LineUp_Request r: list){
            boolean res = reservationManager.verifyAvailability(r);
            System.out.println("La reservation con codice " + r.getCode() + " può entrare: " +res);
            System.out.println("Vuole entrare alle ore: " +r.getStartingTime());
            if(res)
                reservationManager.addRequest(r);
        }

        Timer timer = new Timer();
        TimerTask timerTask1 = createTimerTask(reservationManager, lineUp1);
        TimerTask timerTask2 = createTimerTask(reservationManager, lineUp2);
        TimerTask timerTask3 = createTimerTask(reservationManager, lineUp3);
        TimerTask timerTask4 = createTimerTask(reservationManager, lineUp4);
        TimerTask timerTask5 = createTimerTask(reservationManager, lineUp5);
        TimerTask timerTask6 = createTimerTask(reservationManager, lineUp6);
        TimerTask timerTask7 = createTimerTask(reservationManager, lineUp1);
        TimerTask timerTask8 = createTimerTask(reservationManager, lineUp2);
        TimerTask timerTask9 = createTimerTask(reservationManager, lineUp3);
        TimerTask timerTask10 = createTimerTask(reservationManager, lineUp4);

        timer.schedule(timerTask1, lineUp1.getEntryTime()-actual);
        timer.schedule(timerTask2, lineUp2.getEntryTime()-actual);
        timer.schedule(timerTask3, lineUp3.getEntryTime()-actual);
        timer.schedule(timerTask4, lineUp4.getEntryTime()-actual);
        timer.schedule(timerTask5, lineUp5.getEntryTime()-actual);
        timer.schedule(timerTask6, lineUp6.getEntryTime()-actual);

        timer.schedule(timerTask7, lineUp1.getExitTime()-actual-1000);
        timer.schedule(timerTask8, lineUp2.getExitTime()-actual-1000);
        timer.schedule(timerTask9, lineUp3.getExitTime()-actual-1000);
        timer.schedule(timerTask10, lineUp4.getExitTime()-actual-1000);
        sleep(100);

        storeRepository.delete(store);

    }

    /*
    Simulation of situation with scan and delay in leavings
     */


    @Test
    void complexSituation2() throws InterruptedException {
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(closure-hour);
        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);

        LineUp_Request lineUp1 = createLineUp(0, aperture+minute, minute);
        LineUp_Request lineUp2 = createLineUp(1, aperture+minute*2, minute);
        LineUp_Request lineUp3 = createLineUp(2, aperture+minute*4, minute);
        LineUp_Request lineUp4 = createLineUp(3, aperture+minute*2, minute);
        LineUp_Request lineUp5 = createLineUp(4, aperture+minute*4, minute);
        LineUp_Request lineUp6 = createLineUp(5, aperture+minute*6, minute);
        List<LineUp_Request> list = new ArrayList<>();
        lineUp1.setUserID(0);
        lineUp2.setUserID(1);
        lineUp3.setUserID(2);
        lineUp4.setUserID(3);
        lineUp5.setUserID(4);
        lineUp6.setUserID(5);
        lineUp1.setStoreID(store.getId());
        lineUp2.setStoreID(store.getId());
        lineUp3.setStoreID(store.getId());
        lineUp4.setStoreID(store.getId());
        lineUp5.setStoreID(store.getId());
        lineUp6.setStoreID(store.getId());
        list.add(lineUp1);
        list.add(lineUp2);
        list.add(lineUp3);
        list.add(lineUp4);
        list.add(lineUp5);
        list.add(lineUp6);

        lineUp1.setStoreID(store.getId());
        lineUp2.setStoreID(store.getId());
        lineUp3.setStoreID(store.getId());
        lineUp4.setStoreID(store.getId());
        lineUp5.setStoreID(store.getId());
        lineUp6.setStoreID(store.getId());

        for(LineUp_Request r: list){
            boolean res = reservationManager.verifyAvailability(r);
            System.out.println("La reservation con codice " + r.getCode() + " può entrare: " +res);
            System.out.println("Vuole entrare alle ore: " +r.getStartingTime());
            if(res)
                reservationManager.addRequest(r);
        }

        Timer timer = new Timer();
        TimerTask timerTask1 = createTimerTask(reservationManager, lineUp1);
        TimerTask timerTask2 = createTimerTask(reservationManager, lineUp2);
        TimerTask timerTask3 = createTimerTask(reservationManager, lineUp3);
        TimerTask timerTask4 = createTimerTask(reservationManager, lineUp4);
        TimerTask timerTask5 = createTimerTask(reservationManager, lineUp5);
        TimerTask timerTask6 = createTimerTask(reservationManager, lineUp6);
        TimerTask timerTask7 = createTimerTask(reservationManager, lineUp1);
        TimerTask timerTask8 = createTimerTask(reservationManager, lineUp2);
        TimerTask timerTask9 = createTimerTask(reservationManager, lineUp3);
        TimerTask timerTask10 = createTimerTask(reservationManager, lineUp4);
        TimerTask timerTask11 = createTimerTask(reservationManager, lineUp5);
        TimerTask timerTask12 = createTimerTask(reservationManager, lineUp6);

        timer.schedule(timerTask1, lineUp1.getEntryTime()-actual);
        timer.schedule(timerTask2, lineUp2.getEntryTime()-actual);
        timer.schedule(timerTask3, lineUp3.getEntryTime()-actual);
        timer.schedule(timerTask4, lineUp4.getEntryTime()-actual);
        timer.schedule(timerTask5, lineUp5.getEntryTime()-actual);
        timer.schedule(timerTask6, lineUp6.getEntryTime()-actual);

        timer.schedule(timerTask7, lineUp1.getExitTime()-actual-1000);
        timer.schedule(timerTask8, lineUp2.getExitTime()+2*minute-actual-1000);
        timer.schedule(timerTask9, lineUp3.getExitTime()-actual-1000);
        timer.schedule(timerTask10, lineUp4.getExitTime()+2*minute-actual-1000);
        timer.schedule(timerTask11,lineUp3.getExitTime()+2*minute-actual);
        timer.schedule(timerTask12,lineUp4.getExitTime()+2*minute-actual);
        sleep(100);

    }

    private TimerTask createTimerTask(ReservationManager reservationManager, Reservation r){
        return new TimerTask() {
            @Override
            public void run() {
                //reservationManager.loadLists(r.getStoreID(), Date.valueOf(LocalDate.now()));
                Reservation result = reservationManager.scan(r.getCode());
                if(result!=null)
                    System.out.println("La reservation che ha il code "+ result.getCode() + " è entrata/uscita: ");
                else
                    System.out.println("Non può entrare");
            }
        };
    }


    private LineUp_Request createLineUp(int code, long start, long duration){
        LineUp_Request lineUp_request = new LineUp_Request();
        lineUp_request.setCode(code);
        lineUp_request.setStartingTime(start);
        lineUp_request.setDuration(duration);
        lineUp_request.setDate(Date.valueOf(LocalDate.now()));
        return lineUp_request;
    }

    @Test
    void removeRequest1(){
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(2);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        testEntityManager.persist(store);
        testEntityManager.flush();

        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);

        LineUp_Request lineUp_request = new LineUp_Request();
        lineUp_request.setStoreID(store.getId());
        lineUp_request.setUserID(0);
        lineUp_request.setDate(Date.valueOf(LocalDate.now()));
        lineUp_request.setStartingTime(aperture + 50*minute);
        lineUp_request.setDuration(5*minute);
        lineUp_request.setResType("lineUp_Request");

        reservationManager.loadLists(store.getId(), Date.valueOf(LocalDate.now()));
        reservationManager.addRequest(lineUp_request);
        reservationManager.removeRequest(lineUp_request);

        assertEquals(lineUp_request.getStatus(), "Rejected");
    }

    @Test
    void removeRequest2(){
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        long hour = 3600000;
        long actual = Time.valueOf(lt).getTime() + hour;
        long minute = 60000;
        long closure = actual+hour;
        long aperture = closure-hour;
        Store store = new Store();
        store.setMaxCapability(1);
        store.setClosureTime(closure);
        store.setStartingTime(aperture);
        testEntityManager.persist(store);
        testEntityManager.flush();

        ReservationManager reservationManager = new ReservationManager();
        reservationManager.setStore(store);

        reservationManager.setReservationRepository(reservationRepository);
        reservationManager.setUserRepository(userRepository);
        reservationManager.setStoreRepository(storeRepository);

        LineUp_Request lineUp_request1 = new LineUp_Request();
        lineUp_request1.setStoreID(store.getId());
        lineUp_request1.setUserID(0);
        lineUp_request1.setDate(Date.valueOf(LocalDate.now()));
        lineUp_request1.setStartingTime(aperture + 10*minute);
        lineUp_request1.setDuration(30*minute);
        lineUp_request1.setResType("lineUp_Request");

        LineUp_Request lineUp_request = new LineUp_Request();
        lineUp_request.setStoreID(store.getId());
        lineUp_request.setUserID(1);
        lineUp_request.setDate(Date.valueOf(LocalDate.now()));
        lineUp_request.setStartingTime(aperture + 10*minute);
        lineUp_request.setDuration(5*minute);
        lineUp_request.setResType("lineUp_Request");

        reservationManager.loadLists(store.getId(), Date.valueOf(LocalDate.now()));
        reservationManager.addRequest(lineUp_request1);
        reservationManager.addRequest(lineUp_request);
        reservationManager.removeRequest(lineUp_request);

        assertEquals(lineUp_request.getStatus(), "Rejected");
    }
}
