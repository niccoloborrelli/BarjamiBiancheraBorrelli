package com.example.demo;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Transactional
public interface ReservationRepository extends ReservationBaseRepository<Reservation> {
    List<Reservation> findByDateAndStoreIDAndResTypeAndStatus(Date date, int storeID, String resType, String status);
    List<Reservation> findByUserIDOrderByEntryTime(int userID);
    Reservation findByCodeAndStoreID(int code, int storeID);
    List<Reservation> findByUserIDAndStatus(int userID, String status);
    List<Reservation> findByUserIDAndStatusOrderByEntryTime(int userID, String status);
    List<Reservation> findByDateAndStoreIDAndStatus(Date date, int storeID, String status);
}
