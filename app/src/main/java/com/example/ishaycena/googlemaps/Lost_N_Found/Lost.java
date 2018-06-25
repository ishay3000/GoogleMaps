package com.example.ishaycena.googlemaps.Lost_N_Found;

import com.google.android.gms.maps.model.LatLng;

public class Lost {
    public String person, PlaceName;
    public LatLng latLng;


    public Lost(String person, String placeName, LatLng latLng) {
        this.person = person;
        PlaceName = placeName;
        this.latLng = latLng;
    }
}
