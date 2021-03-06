package com.example.faf_360.models;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Map;

public class App {

    private static App app;

    private App() {
        // Usuario por defecto que esta autenticado
    }

    private FirebaseDatabase fdbSisaber;
    private DatabaseReference dbrSisaber;

    public static App GetApp(Activity activity) {
        if (app == null) {
            app = new App();
            FirebaseAuth mAuth;

            FirebaseApp.initializeApp(activity);

            //Initialize Firebase Db
            app.fdbSisaber = FirebaseDatabase.getInstance();
            app.dbrSisaber = app.fdbSisaber.getReference();

            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();

            // Obtiene el usuario actual
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                DatabaseReference ref = app.dbrSisaber.child("Usuario").child(currentUser.getUid());

                // Se crea el usuario autenticado
                ref.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map singleUser = (Map) dataSnapshot.getValue();
                                Usuarios user = Usuarios.toUser(singleUser);
                                app.setUserLogin(user);
                                app.getAppTohken();
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
    private String tokenApp;

    private void getAppTohken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("GetIDAplication", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        tokenApp = task.getResult().getToken();

                        DatabaseReference ref = app.dbrSisaber.child("Usuario").child(app.userLogin.getId());
                        // Guarda el token de instalación de la App
                        if (app.tokenApp != "")
                            ref.child("tokenApp").setValue(app.tokenApp);
                    }
                });
    }

    public Usuarios getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(Usuarios userLogin) {
        this.userLogin = userLogin;
    }
}