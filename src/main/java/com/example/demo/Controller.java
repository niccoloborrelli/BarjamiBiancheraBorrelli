package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class Controller {

    @Autowired
    private Redirector redirector;

    @PostMapping("/Request")
    String newRequest(@RequestBody RequestMessage newRequestMessage) {
        System.out.println(newRequestMessage.startingTime);
        if(!verifyTimeFormat(newRequestMessage.getStartingTime()) || !verifyTimeFormat(newRequestMessage.getDuration()))
            return "error in format time";
        if(!verifyDateFormat(newRequestMessage.getData()))
            return "error in format data";
        // here the call to the service has to be add , all the information are in newRequestMessage
        if(redirector.transferRequest(newRequestMessage))
            return newRequestMessage.getUsername() + ", your request has been accepted";
        else
            return newRequestMessage.getUsername() + ", your request has been rejected";
        //return "200 ok";
    }

    @GetMapping("/store")
    List<StoreInfoVisualization> getStoreInfo(){
        List<Store> stores = redirector.transferRequest();
        List<StoreInfoVisualization> returnList = new ArrayList<>();
        Store store;
        for(int i = 0; i < 4; i++){
            store = stores.get(i);
            returnList.add(i, new StoreInfoVisualization());
            returnList.get(i).setAddress(store.getAddress());
            returnList.get(i).setChain(store.getChain());
            returnList.get(i).setOpenTime(String.valueOf(store.getStartingTime()));
            returnList.get(i).setClosureTime(String.valueOf(store.getClosureTime()));
        }
        return returnList;
    }

    @GetMapping("/departments")
    List<String> getStoreDepartment(@RequestBody StoreInfoVisualization siv){
        return redirector.transferRequest(siv);
    }

    @GetMapping("/reservations")
    List<ReservationStatusMessage> getReservations(@RequestBody String email){
        List<Reservation> reservations = redirector.tranferRequest(email);
        List<ReservationStatusMessage> returnList = new ArrayList<>();
        Reservation reservation = null;
        System.out.println(reservations);
        if(reservations != null){
            for(int i = 0; i < 4 && i < reservations.size(); i++){
                if(reservations.get(i) != null){
                    reservation = reservations.get(i);
                    Store store = redirector.transferRequest(reservation.getStoreID());
                    returnList.add(i, new ReservationStatusMessage(store.getChain(), store.getAddress(),
                            String.valueOf(reservation.getEntryTime()), String.valueOf(reservation.getCode())));
                }

            }
        }

        System.out.println("in controller reservation: " + reservation);
        return returnList;
    }

    @PostMapping("/loginRequest")
    String login(@RequestBody LoginMessage newLoginMessage) {
        System.out.println(newLoginMessage.username);
        return redirector.transferRequest1(newLoginMessage);
    }

    @PostMapping("/registrationRequest")
    String registration(@RequestBody LoginMessage newLoginMessage) {
        System.out.println("dentro registration in controller");
        boolean registrationResult = redirector.transferRequest2(newLoginMessage);
        if(registrationResult)
            return newLoginMessage.getUsername()+" you registered in correctly";
        else
            return newLoginMessage.getUsername()+" your registration failed";
    }

    @PostMapping("/scanRequest")
    String scan(@RequestBody ScanMessage newScanMessage) {
        boolean scanResult = redirector.transferRequest(newScanMessage);
        if(scanResult)
            return "scanned correctly";
        else
            return "error in scan";
    }

    @PostMapping("/delete")
    String delete(@RequestBody DeleteMessage deleteMessage){
        if(redirector.transferRequest(deleteMessage))
            return "delete completed";
        else
            return "something went wrong";
    }

    @PostMapping("/reservationStateRequest")
    String reservationState(@RequestBody ReservationStateMessage newReservationStateMessage) {
        String reservationState = new String();
        // call for ReservationState service
        return reservationState;
    }

    @PostMapping("/preferredStore")
    String setPreferredStore(@RequestBody StoreMessage storeMessage){
        if(redirector.transferRequest(storeMessage))
            return "added correctly";
        else
            return "something went wrong";
    }

    @GetMapping("/status")
    List<String> getStoreStatus(@RequestBody String emailManager){
        return redirector.transferRequest(emailManager);
    }

    @PostMapping("/create")
    String createStore(@RequestBody StoreCreationMessage storeCreationMessage){
        redirector.transferRequest(storeCreationMessage);
        return "200 OK";
    }


    /** here i verify that the format of the time is xx:xx:xx where x is a number **/
    public boolean verifyTimeFormat(String time){
        System.out.println(time);
        if(time.length()==8)
            if(time.charAt(2)==':'&& time.charAt(5)==':'){
                ArrayList<Integer> indexes= new ArrayList<Integer>();
                indexes.add(0);
                indexes.add(1);
                indexes.add(3);
                indexes.add(4);
                indexes.add(6);
                indexes.add(7);
                for (int i :indexes) {
                    if(time.charAt(i)>'9'||time.charAt(i)<'0')
                        return false;
                }
                return true;
            }
        return false;
    };

    /** here i verify that the format of the data is xxxx-xx-xx where x is a number **/
    public boolean verifyDateFormat(String time){
        if(time.length()==10)
            if(time.charAt(4)=='-'&& time.charAt(7)=='-'){
            ArrayList<Integer> indexes= new ArrayList<Integer>();
            indexes.add(0);
            indexes.add(1);
            indexes.add(2);
            indexes.add(3);
            indexes.add(5);
            indexes.add(6);
            indexes.add(8);
            indexes.add(9);
            for (int i :indexes) {
                if(time.charAt(i)>'9'||time.charAt(i)<'0')
                    return false;
            }
            return true;
        }
        return false;
    };
}
