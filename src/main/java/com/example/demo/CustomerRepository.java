package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface CustomerRepository extends UserBaseRepository<Customer> {

    Customer findByEmailAndPassword(String email, String password);
    Customer findByEmail(String email);
}
