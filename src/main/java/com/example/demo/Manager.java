package com.example.demo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("manager")
@NamedQueries({
        @NamedQuery(name = "Manager.checkCredentials", query = "select u from Manager u where u.email = ?1 and u.password = ?2"),
        @NamedQuery(name = "Manager.searchByEmail", query = "select u from Manager u where u.email = :query")})
public class Manager extends User{
}
