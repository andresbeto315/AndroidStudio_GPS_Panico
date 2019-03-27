package com.example.faf_360;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.example.faf_360.models.UserInfoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UsersPlacesFavoriteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private UserInfoLocation user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_places_favorite);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        user = new UserInfoLocation();
        user.UserName = "Johanna";
        user.Favorites.add(new LatLng(4.668078, -74.0571497));
        user.Favorites.add(new LatLng(4.6458605, -74.0795658));
        user.Favorites.add(new LatLng(4.6494,-74.0794506));
        user.Favorites.add(new LatLng(4.6419359,-74.0790751));
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
        AddMarkers();
        /*
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }


    private void AddMarkers(){
        String myLat="prueba";

        for (LatLng posision : user.Favorites)
        {
            mMap.addMarker(new MarkerOptions().position(posision).title("Su Lugar Favorito"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posision, 15));
        }

    }
}