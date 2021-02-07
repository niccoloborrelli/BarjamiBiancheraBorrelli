package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class RequestManagerTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    LineUpRepository lineUpRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void submitRequest1() {
        Store store = new Store();
        store.setStartingTime(10);
        store.setClosureTime(20);
        store.setAverageVisitDuration(1000);
        store.setChain("Topolinia");
        store.setAddress("Deposito_di_Paperone");

        User u = new Customer();
        u.setEmail("pippo");
        u.setMeanVisitDuration(10);

        testEntityManager.persist(u);
        testEntityManager.persist(store);
        testEntityManager.flush();

        RequestMessage requestMessage = new RequestMessage("pippo", "00:00:00", "2021-02-07", "Topolinia",
                "Deposito_di_Paperone", "lineUp_Request", null, "08:00:00");

        RequestManager requestManager = new RequestManager();
        ReservationManagerComponent reservationManagerComponent = new ReservationManagerComponent();
        requestManager.setStoreRepository(storeRepository);
        requestManager.setReservationRepository(reservationRepository);
        requestManager.setReservationManager(reservationManagerComponent);
        requestManager.setUserRepository(userRepository);
        reservationManagerComponent.setUserRepository(userRepository);
        reservationManagerComponent.setReservationRepository(reservationRepository);
        reservationManagerComponent.setBookingRepository(bookingRepository);
        reservationManagerComponent.setStoreRepository(storeRepository);
        reservationManagerComponent.setLineUpRepository(lineUpRepository);

        assertFalse(requestManager.submitRequest(requestMessage));


    }

    @Test
    void submitRequest2() {
        Store store = new Store();
        store.setStartingTime(10);
        store.setClosureTime(20);
        store.setAverageVisitDuration(0);
        store.setChain("Topolinia");
        store.setAddress("Deposito_di_Paperone");

        User u = new Customer();
        u.setEmail("pippo");

        testEntityManager.persist(u);
        testEntityManager.persist(store);
        testEntityManager.flush();

        RequestMessage requestMessage = new RequestMessage("pippo", "00:20:00", "2021-02-07", "Topolinia",
                "Deposito_di_Paperone", "lineUp_Request", null, "08:00:00");

        RequestManager requestManager = new RequestManager();
        ReservationManagerComponent reservationManagerComponent = new ReservationManagerComponent();
        requestManager.setStoreRepository(storeRepository);
        requestManager.setReservationRepository(reservationRepository);
        requestManager.setReservationManager(reservationManagerComponent);
        requestManager.setUserRepository(userRepository);
        reservationManagerComponent.setUserRepository(userRepository);
        reservationManagerComponent.setReservationRepository(reservationRepository);
        reservationManagerComponent.setBookingRepository(bookingRepository);
        reservationManagerComponent.setStoreRepository(storeRepository);
        reservationManagerComponent.setLineUpRepository(lineUpRepository);

        assertFalse(requestManager.submitRequest(requestMessage));


    }

    @Test
    void submitRequest3() {
        Store store = new Store();
        store.setStartingTime(10);
        store.setClosureTime(20);
        store.setAverageVisitDuration(1000);
        store.setChain("Topolinia");
        store.setAddress("Deposito_di_Paperone");

        User u = new Customer();
        u.setEmail("pippo");

        testEntityManager.persist(u);
        testEntityManager.persist(store);
        testEntityManager.flush();
        u.setMeanVisitDuration(0);

        RequestMessage requestMessage = new RequestMessage("pippo", "00:00:00", "2021-02-07", "Topolinia",
                "Deposito_di_Paperone", "lineUp_Request", null, "08:00:00");

        RequestManager requestManager = new RequestManager();
        ReservationManagerComponent reservationManagerComponent = new ReservationManagerComponent();
        requestManager.setStoreRepository(storeRepository);
        requestManager.setReservationRepository(reservationRepository);
        requestManager.setReservationManager(reservationManagerComponent);
        requestManager.setUserRepository(userRepository);
        reservationManagerComponent.setUserRepository(userRepository);
        reservationManagerComponent.setReservationRepository(reservationRepository);
        reservationManagerComponent.setBookingRepository(bookingRepository);
        reservationManagerComponent.setStoreRepository(storeRepository);
        reservationManagerComponent.setLineUpRepository(lineUpRepository);

        assertFalse(requestManager.submitRequest(requestMessage));


    }

    @Test
    void submitRequest4() {
        Store store = new Store();
        store.setStartingTime(10);
        store.setClosureTime(20);
        store.setAverageVisitDuration(1000);
        store.setChain("Topolinia");
        store.setAddress("Deposito_di_Paperone");

        User u = new Customer();
        u.setEmail("pippo");


        testEntityManager.persist(u);
        testEntityManager.persist(store);
        testEntityManager.flush();

        RequestMessage requestMessage = new RequestMessage("pippo", "00:20:00", "2021-02-07", "Topolinia",
                "Deposito_di_Paperone", "booking_Request", null, "08:00:00");

        RequestManager requestManager = new RequestManager();
        ReservationManagerComponent reservationManagerComponent = new ReservationManagerComponent();
        requestManager.setStoreRepository(storeRepository);
        requestManager.setReservationRepository(reservationRepository);
        requestManager.setReservationManager(reservationManagerComponent);
        requestManager.setUserRepository(userRepository);
        reservationManagerComponent.setUserRepository(userRepository);
        reservationManagerComponent.setReservationRepository(reservationRepository);
        reservationManagerComponent.setBookingRepository(bookingRepository);
        reservationManagerComponent.setStoreRepository(storeRepository);
        reservationManagerComponent.setLineUpRepository(lineUpRepository);

        assertFalse(requestManager.submitRequest(requestMessage));


    }

    @Test
    void submitRequest5() {
        Store store = new Store();
        store.setStartingTime(10);
        store.setClosureTime(20);
        store.setAverageVisitDuration(1000);
        store.setChain("Topolinia");
        store.setAddress("Deposito_di_Paperone");

        User u = new Customer();
        u.setEmail("pippo");
        u.setMeanVisitDuration(100);

        testEntityManager.persist(u);
        testEntityManager.persist(store);
        testEntityManager.flush();

        RequestMessage requestMessage = new RequestMessage("pippo", "00:00:00", "2021-02-07", "Topolinia",
                "Deposito_di_Paperone", "booking_Request", null, "08:00:00");

        RequestManager requestManager = new RequestManager();
        ReservationManagerComponent reservationManagerComponent = new ReservationManagerComponent();
        requestManager.setStoreRepository(storeRepository);
        requestManager.setReservationRepository(reservationRepository);
        requestManager.setReservationManager(reservationManagerComponent);
        requestManager.setUserRepository(userRepository);
        reservationManagerComponent.setUserRepository(userRepository);
        reservationManagerComponent.setReservationRepository(reservationRepository);
        reservationManagerComponent.setBookingRepository(bookingRepository);
        reservationManagerComponent.setStoreRepository(storeRepository);
        reservationManagerComponent.setLineUpRepository(lineUpRepository);

        assertFalse(requestManager.submitRequest(requestMessage));


    }

    @Test
    void submitRequest6() {
        Store store = new Store();
        store.setStartingTime(10);
        store.setClosureTime(20);
        store.setAverageVisitDuration(1000);
        store.setChain("Topolinia");
        store.setAddress("Deposito_di_Paperone");

        User u = new Customer();
        u.setEmail("pippo");
        u.setMeanVisitDuration(0);

        testEntityManager.persist(u);
        testEntityManager.persist(store);
        testEntityManager.flush();

        RequestMessage requestMessage = new RequestMessage("pippo", "00:00:00", "2021-02-07", "Topolinia",
                "Deposito_di_Paperone", "booking_Request", null, "08:00:00");

        RequestManager requestManager = new RequestManager();
        ReservationManagerComponent reservationManagerComponent = new ReservationManagerComponent();
        requestManager.setStoreRepository(storeRepository);
        requestManager.setReservationRepository(reservationRepository);
        requestManager.setReservationManager(reservationManagerComponent);
        requestManager.setUserRepository(userRepository);
        reservationManagerComponent.setUserRepository(userRepository);
        reservationManagerComponent.setReservationRepository(reservationRepository);
        reservationManagerComponent.setBookingRepository(bookingRepository);
        reservationManagerComponent.setStoreRepository(storeRepository);
        reservationManagerComponent.setLineUpRepository(lineUpRepository);

        assertFalse(requestManager.submitRequest(requestMessage));


    }

    @Test
    void deleteRequest() {
        Store store = new Store();
        store.setStartingTime(10);
        store.setClosureTime(20);
        store.setAverageVisitDuration(1000);
        store.setChain("Topolinia");
        store.setAddress("Deposito_di_Paperone");

        User u = new Customer();
        u.setEmail("pippo");

        testEntityManager.persist(u);
        testEntityManager.persist(store);
        testEntityManager.flush();

        RequestMessage requestMessage = new RequestMessage("pippo", "00:00:00", "2021-02-07", "Topolinia",
                "Deposito_di_Paperone", "booking_Request", null, "08:00:00");

        RequestManager requestManager = new RequestManager();
        ReservationManagerComponent reservationManagerComponent = new ReservationManagerComponent();
        requestManager.setStoreRepository(storeRepository);
        requestManager.setReservationRepository(reservationRepository);
        requestManager.setReservationManager(reservationManagerComponent);
        requestManager.setUserRepository(userRepository);
        reservationManagerComponent.setUserRepository(userRepository);
        reservationManagerComponent.setReservationRepository(reservationRepository);
        reservationManagerComponent.setBookingRepository(bookingRepository);
        reservationManagerComponent.setStoreRepository(storeRepository);
        reservationManagerComponent.setLineUpRepository(lineUpRepository);
        assertFalse(requestManager.submitRequest(requestMessage));
    }
}