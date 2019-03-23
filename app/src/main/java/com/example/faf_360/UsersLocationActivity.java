package com.example.faf_360;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.faf_360.common.Firebase;
import com.example.faf_360.common.Permission;
import com.example.faf_360.models.UserInfoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class UsersLocationActivity extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.getMapAsync(this);
        mapFragment.onResume();
    }

    public void SavePosition() {

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMarkerClickListener(this);

        AddMarkerPositionUsers();

        enableMyLocation();

        //mMap.setOnMyLocationChangeListener(this);
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
        List<UserInfoLocation> users = new Firebase().GetUsers();
        for (UserInfoLocation user : users) {
            mMap.addMarker(new MarkerOptions().position(user.Location).title(user.UserName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user.Location, 15));
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
}