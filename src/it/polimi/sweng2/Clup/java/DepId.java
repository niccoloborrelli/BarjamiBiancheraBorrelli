package it.polimi.sweng2.Clup.java;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DepId implements Serializable {
    private String depType;
    private int storeid;

    public DepId() {}

    public String getDepType() {
        return depType;
    }

    public void setDepType(String depType) {
        this.depType = depType;
    }

    public int getStoreId() {
        return storeid;
    }

    public void setStoreId(int storeId) {
        this.storeid = storeId;
    }
}
