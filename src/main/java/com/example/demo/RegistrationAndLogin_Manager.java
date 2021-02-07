package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import java.sql.Time;
import java.util.List;

@Component
public class RegistrationAndLogin_Manager implements RegistrationServiceInt, LoginServiceInt {

    @PersistenceContext
    EntityManager em;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    ScanClerkRepository scanClerkRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreRepository storeRepository;

    @Override
    public boolean register(String email, String password, String type) {
        Customer customer = customerRepository.findByEmail(email);
        Manager manager = managerRepository.findByEmail(email);
        ScanClerk scanClerk = scanClerkRepository.findByEmail(email);

        if(customer != null || manager!=null || scanClerk!=null)
            return false;
        //User u = null;
        switch (type){
            case "customer":
                createNewCustomer(email, password);
                break;
            case "manager":
                createNewManager(email, password);
                break;
            case "scanClerk":
                createNewScanClerk(email, password);
                break;
            case "handout":
                //TODO class handout
        }

        //em.persist(u);
        //em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean registerStore(String address, String chain, String openingTime, String closureTime){
        Store s = new Store();
        s.setAddress(address);
        s.setChain(chain);
        s.setStartingTime(Time.valueOf(openingTime).getTime()+3600000);
        s.setClosureTime(Time.valueOf(closureTime).getTime()+3600000);
        storeRepository.save(s);
        return true;
    }

    private Customer createNewCustomer(String email, String password) {
        Customer c = new Customer();
        c.setType("customer");
        c.setEmail(email);
        c.setPassword(password);
        //c.setDelay(Time.valueOf("00:00:00"));
        c.setDelay(0);
        c.setMeanVisitDuration(0);
        //c.setMeanVisitDuration(Time.valueOf("00:00:00"));
        c = customerRepository.save(c);
        return c;
    }

    private Manager createNewManager(String email, String password){
        Manager m = new Manager();
        m.setType("manager");
        m.setEmail(email);
        m.setPassword(password);
        //m.setDelay(Time.valueOf("00:00:00"));
        //m.setMeanVisitDuration(Time.valueOf("00:00:00"));
        m.setDelay(0);
        m.setMeanVisitDuration(0);
        managerRepository.save(m);
        return m;
    }

    private ScanClerk createNewScanClerk(String email, String password){
        ScanClerk s = new ScanClerk();
        s.setType("scanClerk");
        s.setEmail(email);
        s.setPassword(password);
        //s.setDelay(Time.valueOf("00:00:00"));
        //s.setMeanVisitDuration(Time.valueOf("00:00:00"));
        s.setDelay(0);
        s.setMeanVisitDuration(0);
        scanClerkRepository.save(s);
        return s;
    }

    @Override
    public String login(String email, String password) {
        Customer customer = customerRepository.findByEmailAndPassword(email, password);
        Manager manager = managerRepository.findByEmailAndPassword(email, password);
        ScanClerk scanClerk = scanClerkRepository.findByEmailAndPassword(email, password);
        if(customer != null)
            return "customer";
        else if(manager != null)
            return "manager";
        else if(scanClerk != null)
            return "scanClerk";
        else return null;
    }
}
