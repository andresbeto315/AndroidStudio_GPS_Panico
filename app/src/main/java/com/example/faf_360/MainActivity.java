package com.example.faf_360;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.faf_360.models.Usuarios;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
    }

    private void InitFirebase() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void Guardar (View view) {
        Usuarios user = null;

        user = new Usuarios();
        user.setId(UUID.randomUUID().toString());
        user.setFirstname("John");
        user.setLastname("Caicedo");
        user.setEmail("jc@gmail.com");
        user.setPassword("123456");
        user.setIsConnected(true);
        user.setLocation(new LatLng(4.668078, -74.0571497));
        databaseReference.child("Prueba").child(user.getId()).setValue(user);

        user = new Usuarios();
        user.setId(UUID.randomUUID().toString());
        user.setFirstname("Andres");
        user.setLastname("Cortes");
        user.setEmail("ac@gmail.com");
        user.setPassword("Abc123*");
        user.setIsConnected(true);
        user.setLocation(new LatLng(4.6706675, -74.0576822));
        databaseReference.child("Prueba").child(user.getId()).setValue(user);

        user = new Usuarios();
        user.setId(UUID.randomUUID().toString());
        user.setFirstname("Maida");
        user.setLastname("Gomez");
        user.setEmail("mg@gmail.com");
        user.setPassword("112233");
        user.setIsConnected(true);
        user.setLocation(new LatLng(4.6683494, -74.0578559));
        databaseReference.child("Prueba").child(user.getId()).setValue(user);

        user = new Usuarios();
        user.setId(UUID.randomUUID().toString());
        user.setFirstname("Mauricio");
        user.setLastname("");
        user.setEmail("ma@gmail.com");
        user.setPassword("qwerty");
        user.setIsConnected(false);
        user.setLocation(new LatLng(4.6660351, -74.0579916));
        databaseReference.child("Prueba").child(user.getId()).setValue(user);

        user = new Usuarios();
        user.setId(UUID.randomUUID().toString());
        user.setFirstname("Ricardo");
        user.setLastname("Acero");
        user.setEmail("ra@gmail.com");
        user.setPassword("lalala");
        user.setIsConnected(false);
        user.setLocation(new LatLng(4.672192, -74.0614247));
        databaseReference.child("Prueba").child(user.getId()).setValue(user);

        Toast.makeText(getApplication(), "Guardado", Toast.LENGTH_LONG).show();
    }

    protected void onStart()
    {
        super.onStart();
        InitFirebase();

    }
}