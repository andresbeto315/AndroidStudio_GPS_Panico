package com.example.faf_360.common;

import android.app.Activity;

import com.example.faf_360.models.Usuarios;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Firebase {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public Firebase(Activity activity)
    {
        FirebaseApp.initializeApp(activity);
        InitFirebase();
    }

    private void InitFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


/*
    public List<UserInfoLocation> GetUsersOld() {
        List<UserInfoLocation> users = new ArrayList<>();
        UserInfoLocation user;

        user = new UserInfoLocation();
        user.UserName = "Johanna";
        user.IsConnected= false;
        user.Location = new LatLng(4.668078, -74.0571497);
        users.add(user);

        user = new UserInfoLocation();
        user.UserName = "John";
        user.Location = new LatLng(4.6706675, -74.0576822);
        users.add(user);

        user = new UserInfoLocation();
        user.UserName = "Ricardo";
        user.Location = new LatLng(4.6683494, -74.0578559);
        users.add(user);

        user = new UserInfoLocation();
        user.UserName = "Mauricio";
        user.Location = new LatLng(4.6660351, -74.0579916);
        users.add(user);

        user = new UserInfoLocation();
        user.UserName = "Andres";
        user.IsConnected= false;
        user.Location = new LatLng(4.672192, -74.0614247);
        users.add(user);

        return users;
    }
*/
}