package com.example.demo;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ScanClerkRepository extends UserBaseRepository<ScanClerk>{
    ScanClerk findByEmailAndPassword(String email, String password);
    ScanClerk findByEmail(String email);
}
