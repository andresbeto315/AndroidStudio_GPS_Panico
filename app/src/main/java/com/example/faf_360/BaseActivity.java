package com.example.faf_360;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.faf_360.models.App;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitFirebase();
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

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void InitFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
        if (App.GetApp(this).getUserLogin() != null)
            databaseReference.child("Usuario").child(App.GetApp(this).getUserLogin().getId()).child("isConnected").setValue(false);
    }
}