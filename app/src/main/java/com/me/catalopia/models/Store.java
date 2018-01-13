package com.me.catalopia.models;

/**
 * Created by Esraa on 1/13/2018.
 */

public class Store {
    private String id;
    private String name;
    private String description;

    public Store(String id, String name, String desciption) {
        this.id = id;
        this.name = name;
        this.description = desciption;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return getName();
    }
}
