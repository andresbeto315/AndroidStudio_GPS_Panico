package com.example.faf_360.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class UserInfoLocation {

    public String UserName;
    public LatLng Location;
    public List<LatLng> Favorites;
    public boolean IsConnected;

    public UserInfoLocation()
    {
        IsConnected = true;
        Favorites = new ArrayList<LatLng>();
    }

}