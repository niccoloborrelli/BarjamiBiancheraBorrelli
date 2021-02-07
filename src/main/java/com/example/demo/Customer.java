package com.example.demo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("customer")
@NamedQueries({
        @NamedQuery(name = "Customer.checkCredentials", query = "select u from Customer u where u.email = ?1 and u.password = ?2"),
        @NamedQuery(name = "Customer.searchByEmail", query = "select u from Customer u where u.email = :query"),
        @NamedQuery(name = "Customer.searchByID", query = "select u from Customer u where u.userID = :query")})
public class Customer extends User {
}
