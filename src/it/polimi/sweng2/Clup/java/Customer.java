package it.polimi.sweng2.Clup.java;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("customer")
@NamedQuery(name = "Customer.checkCredentials", query = "select u from Customer u where u.email = ?1 and u.password = ?2")
@NamedQuery(name = "Customer.searchByEmail", query = "select u from Customer u where u.email = :query") //to search if another user has already registered this email
public class Customer extends User{
}
