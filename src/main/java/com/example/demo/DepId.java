package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DepId implements Serializable {

    private String dep_type;
    private int storeid;

    public DepId() {
    }

    public String getDepType() {
        return dep_type;
    }

    public void setDepType(String depType) {
        this.dep_type = depType;
    }

    public int getStoreId() {
        return storeid;
    }

    public void setStoreId(int storeId) {
        this.storeid = storeId;
    }

    @Override
    public boolean equals(Object o){
        return true;
    }

    @Override
    public int hashCode(){
        return 0;
    }
}
