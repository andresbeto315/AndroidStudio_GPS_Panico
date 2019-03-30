package com.example.faf_360;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.faf_360.models.Usuarios;
import com.example.faf_360.utils.spUsuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPasswordActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegisterPassword";
    private EditText editTextPasswordPassword;

    private FirebaseDatabase fdbSisaber;
    private DatabaseReference dbrSisaber;
    private FirebaseAuth mAuth;

    spUsuarios spUsuarios = new spUsuarios(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);

        editTextPasswordPassword = findViewById(R.id.editTextPasswordPassword);
        findViewById(R.id.buttonPasswordCreateProfile).setOnClickListener(this);

        InitFirebase();
    }

    private void InitFirebase() {
        try {
            FirebaseApp.initializeApp(this);

            //Initialize Firebase Db
            fdbSisaber = FirebaseDatabase.getInstance();
            dbrSisaber = fdbSisaber.getReference();

            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
        } catch (Exception e)
        {
            Toast.makeText(getApplication(),
                    getString(R.string.error_database),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.buttonPasswordCreateProfile) {
            createAccount(spUsuarios.getEmail(), editTextPasswordPassword.getText().toString());
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(spUsuarios.getEmail(), editTextPasswordPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            SaveUser(user.getUid());
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            Toast.makeText(RegisterPasswordActivity.this,
                                    getString(R.string.auth_failed) + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                            updateUI(null);
                        }

                        hideProgressDialog();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String password = editTextPasswordPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPasswordPassword.setError(getString(R.string.validate_required));
            valid = false;
        } else {
            editTextPasswordPassword.setError(null);
        }
        if (password.length() <= 5){
            editTextPasswordPassword.setError(getString(R.string.validate_password_error));
            valid = false;
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            if (!user.isEmailVerified()) {

            }
        }
    }

    private void SaveUser(String id){
        Usuarios usuario = spUsuarios.getUsuarios();

        usuario.setId(id);
        usuario.setPassword(editTextPasswordPassword.getText().toString());

        dbrSisaber.child("Usuario").child(usuario.getId()).setValue(usuario);

        spUsuarios.saveId(id);
    }

    public void Regresar(View view) {
        finish();
    }
}
