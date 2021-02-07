package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class Redirector {

    @Autowired
    private FunctionalityRouter functionalityRouter;

    @Autowired
    RegistrationServiceInt registrationServiceInt;

    @Autowired
    LoginServiceInt loginServiceInt;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    StoreManagerRepository storeManagerRepository;

    @Autowired
    UserRepository userRepository;

    public String transferRequest1(LoginMessage loginMessage){
        System.out.println("Login");
        return loginServiceInt.login(loginMessage.getUsername(), loginMessage.getPassword());
    }

    public boolean transferRequest2(LoginMessage loginMessage){
        System.out.println("Registration");
        return registrationServiceInt.register(loginMessage.getUsername(), loginMessage.getPassword(), loginMessage.getType());
    }

    public boolean transferRequest(RequestMessage requestMessage){
        return functionalityRouter.transferRequest(requestMessage);
    }
    public void transferRequest(ReservationStateMessage reservationStateMessage){
        functionalityRouter.transferRequest(reservationStateMessage);
    }
    public boolean transferRequest(ScanMessage scanMessage){
        return functionalityRouter.transferRequest(scanMessage);
    }
    public List<Store> transferRequest(){
        return storeRepository.findAll();
    }

    public List<String> transferRequest(StoreInfoVisualization storeInfoVisualization){
        Store store = storeRepository.findByChainAndAddress(storeInfoVisualization.getChain(), storeInfoVisualization.getAddress());
        List<Department> list =  departmentRepository.findByDepId_Storeid(store.getId());
        List<String> returnList = new ArrayList<>();
        for (Department d: list) {
            returnList.add(d.getId().getDepType());
        }
        return returnList;
    }

    public List<Reservation> tranferRequest(String email){
        Customer c = customerRepository.findByEmail(email);
        return reservationRepository.findByUserIDAndStatusOrderByEntryTime(c.getUserID(), "InTheQueue");
    }

    public Store transferRequest(int storeId){
        return storeRepository.findById(storeId);
    }

    public boolean transferRequest(DeleteMessage deleteMessage){
        return functionalityRouter.transferRequest(deleteMessage);
    }

    public List<String> transferRequest(String email){
        Manager manager = storeManagerRepository.findByEmail(email);
        Store store = storeRepository.findById(manager.getStoreID());
        return functionalityRouter.transferRequest(manager.getStoreID());
    }

    public boolean transferRequest(StoreMessage storeMessage){
        Store store = storeRepository.findByChainAndAddress(storeMessage.getChain(), storeMessage.getAddress());
        User u = userRepository.findByEmail(storeMessage.getEmail());
        if(u != null && store != null){
            u.setStoreID(store.getId());
            userRepository.save(u);
            return true;
        }
        else
            return false;

    }

    public boolean transferRequest(StoreCreationMessage storeCreationMessage){
        return registrationServiceInt.registerStore(storeCreationMessage.getAddress(), storeCreationMessage.getAddress(),
                storeCreationMessage.getOpeningTime(), storeCreationMessage.getClosureTime());
    }
}
