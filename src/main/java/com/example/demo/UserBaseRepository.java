package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends CrudRepository<T, Integer> {
    T findByUserID(int id);
    @Query("select u from #{#entityName} as u where u.email=?1 and u.password=?2")
    T findByEmailAndPassword(String email, String password);
}
