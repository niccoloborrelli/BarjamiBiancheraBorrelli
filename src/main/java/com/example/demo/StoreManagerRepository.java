package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface StoreManagerRepository extends CrudRepository<Manager, Integer> {
    Manager findByEmail(String email);
}
