package com.example.faf_360;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.faf_360.models.Usuarios;
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

    public void Guardar (View view)
    {
        Usuarios user = new Usuarios();

        user.setId(UUID.randomUUID().toString());
        user.setFirstname("John");
        user.setLastname("Caicedo");
        user.setEmail("jc@gmail.com");
        user.setPassword("123456");

        databaseReference.child("Prueba").child(user.getId()).setValue(user);

        Toast.makeText(getApplication(),
                "Guardado",
                Toast.LENGTH_LONG).show();
    }

    protected void onStart()
    {
        super.onStart();
        InitFirebase();

    }
}