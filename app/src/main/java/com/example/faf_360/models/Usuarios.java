package com.example.faf_360.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuarios {

    private String id;
    private String Firstname;
    private String Lastname;
    private String Email;
    private String Password;
    private LatLng Location;
    private List<LatLng> Favorites;
    private boolean IsConnected;
    private boolean HelpMe;

    public Usuarios() {
        IsConnected = true;
        Favorites = new ArrayList<LatLng>();
        HelpMe = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public LatLng getLocation() {
        return Location;
    }

    public void setLocation(LatLng location) {
        Location = location;
    }

    public boolean getIsConnected() {
        return IsConnected;
    }

    public void setIsConnected(boolean isConnected) {
        IsConnected = isConnected;
    }

    public boolean getHelpMe() {
        return HelpMe;
    }

    public void setHelpMe(boolean helpMe) {
        HelpMe = helpMe;
    }

    @Override
    public String toString() {
        return Firstname + " " + Lastname;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("firstname", Firstname);
        result.put("lastname", Lastname);
        result.put("email", Email);
        result.put("password", Password);
        result.put("helpMe", HelpMe);

        return result;
    }

    public static Usuarios toUser(Map userMap) {
        Usuarios user = new Usuarios();
        user.setId(userMap.get("id").toString());
        user.setFirstname(userMap.get("firstname").toString());
        user.setLastname(userMap.get("lastname").toString());
        user.setEmail(userMap.get("email").toString());
        user.setPassword(userMap.get("password").toString());
        user.setIsConnected((boolean)userMap.get("isConnected"));
        Map location = (Map)userMap.get("location");
        if (location != null)
            user.setLocation(new LatLng((double)location.get("latitude"), (double)location.get("longitude")));
        if (userMap.get("helpMe") != null)
            user.setHelpMe((boolean)userMap.get("helpMe"));
        return user;
    }

}