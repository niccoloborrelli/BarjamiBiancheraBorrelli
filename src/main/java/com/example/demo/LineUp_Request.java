package com.example.demo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("lineUp_Request")
@NamedQuery(name = "LineUp_Request.findByDate", query = "select l from LineUp_Request l where l.date = :query")
public class LineUp_Request extends Reservation {

}
