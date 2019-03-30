package com.example.faf_360;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.faf_360.common.Permission;
import com.example.faf_360.models.App;
import com.example.faf_360.models.Usuarios;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UsersLocationActivity extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationChangeListener{

    private GoogleMap mMap;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private List<Usuarios> Users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_location);

        this.Users = new ArrayList<Usuarios>();

        FirebaseApp.initializeApp(this);
        InitFirebase();
        App.GetApp(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.getMapAsync(this);
        mapFragment.onResume();
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        if (App.GetApp(this).getUserLogin() != null)
            databaseReference.child("Usuario").child(App.GetApp(this).getUserLogin().getId()).child("isConnected").setValue(true);
    }
*/
    @Override
    protected void onPause() {
        super.onPause();
        if (App.GetApp(this).getUserLogin() != null)
            databaseReference.child("Usuario").child(App.GetApp(this).getUserLogin().getId()).child("isConnected").setValue(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.GetApp(this).getUserLogin() != null)
            databaseReference.child("Usuario").child(App.GetApp(this).getUserLogin().getId()).child("isConnected").setValue(true);
    }

    private void InitFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        DatabaseReference ref = databaseReference.child("Usuario");
        //ref.addListenerForSingleValueEvent(
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectUsers((Map<String, Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                }
        );
    }

    private void collectUsers(Map<String,Object> users) {
        this.Users.clear();
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            Usuarios user = Usuarios.toUser(singleUser);
            this.Users.add(user);
        }
        AddMarkerPositionUsers();
    }

    public void SavePosition(View view) {
        CameraPosition cp = mMap.getCameraPosition();
        LatLng favorite = new LatLng(cp.target.latitude, cp.target.longitude);
        databaseReference.child("FavoritePlaces").child(App.GetApp(this).getUserLogin().getId()).child(UUID.randomUUID().toString()).setValue(favorite);
        Toast.makeText(this, "Un nuevo favorito ha sido guardado.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationChangeListener(this);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(UsersLocationActivity.this, UsersPlacesFavoriteActivity.class);
                intent.putExtra("userId", marker.getSnippet());
                UsersLocationActivity.this.startActivity(intent);
            }
        });

        enableMyLocation();
    }

    @SuppressLint("MissingPermission")
    public void enableMyLocation() {
        if (Permission.Location(this)) {
            if (mMap != null) {
                Toast.makeText(this, "Si tengo permiso para acceder a la ubicación", Toast.LENGTH_SHORT).show();
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    private void AddMarkerPositionUsers() {
        mMap.clear();
        for (Usuarios user : this.Users) {
            if (user.getLocation() != null) {
                if (!user.getIsConnected()) {
                    mMap.addMarker(new MarkerOptions()
                            .snippet(user.getId())
                            .position(user.getLocation())
                            .title(user.toString())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                } else {
                    mMap.addMarker(new MarkerOptions()
                            .snippet(user.getId())
                            .position(user.getLocation())
                            .title(user.toString()));
                }
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user.getLocation(), 15));
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Click en el boton de mi ubicación", Toast.LENGTH_SHORT).show();
        // Devuelve false para que no consumamos el evento y el comportamiento predeterminado todavía ocurra
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Desde aca vamos a ver los favoritos de este usuario", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String UserName = marker.getTitle();
        Toast.makeText(this,"Hola " + UserName + " estamos listos para ver tus favoritos.",
                Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            if (App.GetApp(this).getUserLogin() != null)
                databaseReference.child("Usuario").child(App.GetApp(this).getUserLogin().getId()).child("location").setValue(position);
        }
    }
}