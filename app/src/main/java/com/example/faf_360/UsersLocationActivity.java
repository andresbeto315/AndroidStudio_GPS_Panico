package com.example.faf_360;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.faf_360.common.Permission;
import com.example.faf_360.models.App;
import com.example.faf_360.models.Usuarios;
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

        if (App.GetApp(this).getUserLogin() != null)
            databaseReference.child("Usuario").child(App.GetApp(this).getUserLogin().getId()).child("isConnected").setValue(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.onCreate(savedInstanceState);
        mapFragment.getMapAsync(this);
        mapFragment.onResume();

        InitPanico();
    }

    private Vibrator vibrator;

    private void setTextoPanico()
    {
        Button button;
        button = findViewById(R.id.btnPanico);
        if (App.GetApp(this).getUserLogin().getHelpMe())
        {
            button.setText(R.string.seguro);
        }
        else
        {
            button.setText(R.string.auxilio);
        }
    }

    public static void setStatusBarColor(Activity activity, int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();

            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }

            window.setStatusBarColor(statusBarColor);
        }
    }

    public void EntraronEnPanico()
    {
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(200);
        }
    }

    public void InitPanico() {
        if (Permission.Vibrator(this)) {
            Toast.makeText(this, "Permite la vibración", Toast.LENGTH_SHORT).show();
        }
        Button button;
        button = findViewById(R.id.btnPanico);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextoPanico();
                if (App.GetApp(UsersLocationActivity.this).getUserLogin().getHelpMe())
                {
                    App.GetApp(UsersLocationActivity.this).getUserLogin().setHelpMe(false);
                    databaseReference.child("Usuario").child(App.GetApp(UsersLocationActivity.this).getUserLogin().getId()).child("helpMe").setValue(false);
                }
                else
                {
                    App.GetApp(UsersLocationActivity.this).getUserLogin().setHelpMe(true);
                    databaseReference.child("Usuario").child(App.GetApp(UsersLocationActivity.this).getUserLogin().getId()).child("helpMe").setValue(true);
                }
            }
        });
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

    private void InitFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void collectUsers(Map<String,Object> users) {
        boolean EnPanico = false;
        this.Users.clear();
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            Usuarios user = Usuarios.toUser(singleUser);
            if (user.getHelpMe()) {
                Toast.makeText(this, user + " esta en peligro!! Activo su botón de pánico!!", Toast.LENGTH_SHORT).show();
                EntraronEnPanico();
                EnPanico = true;
            }
            this.Users.add(user);
        }
        int color = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        if (EnPanico) {
            color = ContextCompat.getColor(this, R.color.colorPanico);
        }
        setStatusBarColor(this, color);
        this.setTextoPanico();
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
        if (mMap != null) {
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