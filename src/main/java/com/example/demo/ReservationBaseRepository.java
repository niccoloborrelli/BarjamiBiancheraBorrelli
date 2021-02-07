package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ReservationBaseRepository<T extends Reservation> extends CrudRepository<T, Integer> {
}
