package com.example.demo;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Transactional
public interface LineUpRepository extends ReservationBaseRepository<LineUp_Request> {
    List<LineUp_Request> findByDateAndStoreID(Date date, int storeID);
}
