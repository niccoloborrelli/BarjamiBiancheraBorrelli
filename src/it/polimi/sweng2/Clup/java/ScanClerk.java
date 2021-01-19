package it.polimi.sweng2.Clup.java;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ScanClerk")
public class ScanClerk extends User{
}
