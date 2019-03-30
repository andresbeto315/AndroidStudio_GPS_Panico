package com.example.faf_360;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.faf_360.utils.spUsuarios;

public class RegisterEmailActivity extends AppCompatActivity {
    spUsuarios spUsuarios = new spUsuarios(this);
    private EditText etRegisterEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);

        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterEmail.setText(spUsuarios.getEmail());
    }

    public void Continue(View view)
    {
        if (etRegisterEmail.getText().toString().isEmpty()) {
            etRegisterEmail.setError(getString(R.string.validate_required));
        } else {
            spUsuarios.saveEmail(etRegisterEmail.getText().toString());

            Intent intent = new Intent(this, RegisterPasswordActivity.class);
            startActivity(intent);
        }
    }
}
