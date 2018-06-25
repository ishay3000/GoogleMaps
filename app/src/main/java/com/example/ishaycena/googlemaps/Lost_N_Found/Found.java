package com.example.ishaycena.googlemaps.Lost_N_Found;

import com.example.ishaycena.googlemaps.Models.PlaceInfo;
import com.google.android.gms.location.places.Place;

public class Found {
    private String name, description;
    private PlaceInfo place;

    public Found(String name, String description, PlaceInfo place) {
        this.name = name;
        this.description = description;
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlaceInfo getPlace() {
        return place;
    }

    public void setPlace(PlaceInfo place) {
        this.place = place;
    }
}
