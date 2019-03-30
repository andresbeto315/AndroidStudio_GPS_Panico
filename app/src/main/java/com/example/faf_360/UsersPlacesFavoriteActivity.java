package com.example.faf_360;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.faf_360.models.App;
import com.example.faf_360.models.Usuarios;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UsersPlacesFavoriteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private List<LatLng> Favorites;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_places_favorite);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("userId");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        Favorites = new ArrayList<LatLng>();

        FirebaseApp.initializeApp(this);
        InitFirebase();

        // Se crea el usuario autenticado
        DatabaseReference ref = databaseReference.child("FavoritePlaces").child(id);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectLocations((Map<String, Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                }
        );
    }

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

    public void Volver(View view)
    {
        Intent intent = new Intent(UsersPlacesFavoriteActivity.this, UsersLocationActivity.class);
        UsersPlacesFavoriteActivity.this.startActivity(intent);
    }

    private void collectLocations(Map<String,Object> users) {
        if (users != null) {
            for (Map.Entry<String, Object> entry : users.entrySet()) {
                Map mapFavorite = (Map) entry.getValue();
                this.Favorites.add(new LatLng((double) mapFavorite.get("latitude"), (double) mapFavorite.get("longitude")));
            }
        }
        AddMarkers();
    }

    private void InitFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
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
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private void AddMarkers() {
        for (LatLng posision : Favorites) {
            mMap.addMarker(new MarkerOptions().position(posision).title("Su Lugar Favorito"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posision, 15));
        }
    }
}