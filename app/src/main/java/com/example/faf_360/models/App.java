package com.example.faf_360.models;

import android.app.Activity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class App {

    private static App app;

    private App()
    {
        // Usuario por defecto que esta autenticado
    }

    public static App GetApp(Activity activity) {
        if (app == null) {
            app = new App();
            FirebaseDatabase fdbSisaber;
            DatabaseReference dbrSisaber;
            FirebaseAuth mAuth;

            FirebaseApp.initializeApp(activity);

            //Initialize Firebase Db
            fdbSisaber = FirebaseDatabase.getInstance();
            dbrSisaber = fdbSisaber.getReference();

            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();

            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                // Se crea el usuario autenticado
                DatabaseReference ref = dbrSisaber.child("Usuario").child(currentUser.getUid());
                ref.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map singleUser = (Map) dataSnapshot.getValue();
                                Usuarios user = Usuarios.toUser(singleUser);
                                app.setUserLogin(user);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        }
                );
            }
        }
        return app;
    }

    private Usuarios userLogin;

    public Usuarios getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(Usuarios userLogin) {
        this.userLogin = userLogin;
    }
}