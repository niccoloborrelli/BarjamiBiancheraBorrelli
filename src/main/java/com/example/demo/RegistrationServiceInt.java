package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationServiceInt {

    boolean register(String email, String password, String type);
    boolean registerStore(String address, String chain, String openingTime, String closureTime);
}
