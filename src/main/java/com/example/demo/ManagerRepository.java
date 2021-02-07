package com.example.demo;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ManagerRepository extends UserBaseRepository<Manager>{
    Manager findByEmailAndPassword(String email, String password);
    Manager findByEmail(String email);
}
