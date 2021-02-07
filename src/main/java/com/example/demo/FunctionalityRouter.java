package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Component
public class FunctionalityRouter {

    @Autowired
    CustomerServiceInt customerServiceInt;

    @Autowired
    StoreManagerServiceInt storeManagerServiceInt;

    @Autowired
    ScanClerkServiceInt scanClerkServiceInt;

    @Autowired
    ScanClerkRepository scanClerkRepository;

    @Autowired
    StoreRepository storeRepository;

    public boolean transferRequest(RequestMessage requestMessage){
        return customerServiceInt.submitRequest(requestMessage);

    }

    public boolean transferRequest(DeleteMessage deleteMessage){
        return customerServiceInt.deleteRequest(deleteMessage);
    }

    public void transferRequest(ReservationStateMessage reservationStateMessage){
    }

    public boolean transferRequest(ScanMessage scanMessage){
        ScanClerk scanClerk = scanClerkRepository.findByEmail(scanMessage.getEmail());
        Store s = storeRepository.findById(scanClerk.getStoreID());
        if(s!=null) {
            return scanClerkServiceInt.scan(scanMessage.getCode(), scanClerk.getStoreID());
        }
        return false;
    }

    public List<String> transferRequest(int storeID){
        Store s = storeRepository.findById(storeID);
        List<String> interi = new ArrayList<>();
        if(s!=null) {
            interi.add(0, String.valueOf(storeManagerServiceInt.showNInside(storeID)));
            interi.add(1, String.valueOf(storeManagerServiceInt.showNLineUp(storeID)));
            interi.add(2, String.valueOf(storeManagerServiceInt.showNBooking(storeID)));
        }else{
            interi.add("0");
            interi.add("0");
            interi.add("0");
        }
        return interi;
    }
}
