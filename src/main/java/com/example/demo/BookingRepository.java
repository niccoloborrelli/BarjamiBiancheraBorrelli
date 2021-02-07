package com.example.demo;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Transactional
public interface BookingRepository extends ReservationBaseRepository<BookingRequest> {
    List<BookingRequest> findByDateAndStoreID(Date date, int storeID);
}
