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
class AccountManagerTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void showCurrentReservation() {
        AccountManager accountManager = new AccountManager();
        accountManager.setReservationRepository(reservationRepository);
        accountManager.setStoreRepository(storeRepository);
        accountManager.setCustomerRepository(customerRepository);

        Customer u = new Customer();
        u.setEmail("pippo");
        testEntityManager.persist(u);

        LineUp_Request lineUp_request = new LineUp_Request();
        lineUp_request.setUserID(u.getUserID());
        testEntityManager.persist(lineUp_request);

        accountManager.showCurrentReservation(u.getEmail());
    }

    @Test
    void updateProfile() {
        AccountManager accountManager = new AccountManager();
        accountManager.setReservationRepository(reservationRepository);
        accountManager.setStoreRepository(storeRepository);
        accountManager.setCustomerRepository(customerRepository);

        Customer u = new Customer();
        u.setEmail("pippo");
        testEntityManager.persist(u);

        Store s = new Store();
        testEntityManager.persist(s);

        LineUp_Request lineUp_request = new LineUp_Request();
        lineUp_request.setUserID(u.getUserID());
        lineUp_request.setStoreID(s.getId());
        lineUp_request.setEntryTime(100);
        lineUp_request.setExitTime(1000);
    }
}