package com.example.demo;

import java.util.List;

public interface CustomerProfile {

    List<Reservation> showCurrentReservation(String email);

}
