package com.example.demo;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends UserBaseRepository<User> {
    User findByEmail(String email);
}
