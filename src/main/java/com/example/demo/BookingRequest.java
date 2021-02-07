package com.example.demo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("booking_Request")
public class BookingRequest extends Reservation{
}
