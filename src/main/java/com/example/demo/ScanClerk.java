package com.example.demo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("scanClerk")
@NamedQueries({
        @NamedQuery(name = "ScanClerk.checkCredentials", query = "select u from ScanClerk u where u.email = ?1 and u.password = ?2"),
        @NamedQuery(name = "ScanClerk.searchByEmail", query = "select u from ScanClerk u where u.email = :query")})
public class ScanClerk extends User {
}
