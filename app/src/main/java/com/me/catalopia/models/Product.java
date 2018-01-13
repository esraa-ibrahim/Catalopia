package com.me.catalopia.models;

import java.io.Serializable;

/**
 * Created by Esraa on 1/11/2018.
 */

public class Product implements Serializable {
    private String id;
    private String mName;
    private String mDescription;
    private Double mPrice;
    private Location mLatLng;

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Product(String name, String description, Double price, Location location) {
        this.mName = name;
        this.mDescription = description;
        this.mPrice = price;
        this.mLatLng = location;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Double getPrice() {
        return mPrice;
    }

    public void setPrice(Double mPrice) {
        this.mPrice = mPrice;
    }

    public Location getLocation() {
        return mLatLng;
    }

    public void setLocation(Location latLng) {
        this.mLatLng = latLng;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            Product p = ((Product) obj);
            return p.id.equals(this.id);
        }
        return false;
    }
}
