package com.example.faf_360;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.faf_360.utils.spUsuarios;

public class RegisterNameActivity extends AppCompatActivity {
    spUsuarios spUsuarios = new spUsuarios(this);

    private EditText etRegisterNameFirstName;
    private EditText etRegisterNameLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);

        etRegisterNameFirstName = findViewById(R.id.etRegisterNameFirstName);
        etRegisterNameFirstName.setText(spUsuarios.getFirstName());

        etRegisterNameLastName = findViewById(R.id.etRegisterNameLastName);
        etRegisterNameLastName.setText(spUsuarios.getLastName());
    }

    public void Continue(View view)
    {
        if (etRegisterNameFirstName.getText().toString().isEmpty()) {
            etRegisterNameFirstName.setError(getString(R.string.validate_required));
        } else if (etRegisterNameLastName.getText().toString().isEmpty()) {
            etRegisterNameLastName.setError(getString(R.string.validate_required));
        } else {
            spUsuarios.saveFirstName(etRegisterNameFirstName.getText().toString());
            spUsuarios.saveLastName(etRegisterNameLastName.getText().toString());

            Intent intent = new Intent(this, RegisterEmailActivity.class);
            startActivity(intent);
        }
    }
}
