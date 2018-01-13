package com.me.catalopia.models;

import java.io.Serializable;

/**
 * Created by Esraa on 1/11/2018.
 */

public class Location implements Serializable {
    private double mLat;
    private double mLng;

    public Location() {

    }

    public Location(double lat, double lng) {
        this.mLat = lat;
        this.mLng = lng;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double mLat) {
        this.mLat = mLat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double mLng) {
        this.mLng = mLng;
    }
}
