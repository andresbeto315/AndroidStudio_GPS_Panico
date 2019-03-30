package com.example.faf_360.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.faf_360.models.Usuarios;

public class spUsuarios {
    private Context mContext;

    final String REGISTER = "register";
    final String DEFVALUE = "";
    final String ID = "id";
    final String AGE = "age";
    final String FIRSTNAME = "firstname";
    final String LASTNAME = "lastname";
    final String EMAIL = "email";
    final String ISCONNECTED = "isConnected";

    public spUsuarios(Context pContext) {
        mContext = pContext;
    }

    private SharedPreferences getSP() {
        return mContext.getSharedPreferences(REGISTER, Context.MODE_PRIVATE);
    }

    private boolean saveSP (String key, String value){
        boolean bRetorno = true;

        try {
            SharedPreferences.Editor spEditor = getSP().edit();
            spEditor.putString(key, value);
            spEditor.commit();
        }
        catch (Exception e) {
            bRetorno = false;
        }

        return bRetorno;
    }

    public String getId() {
        return getSP().getString(ID, DEFVALUE);
    }

    public boolean saveId(String id) {
        return saveSP(ID, id);
    }

    public String getAge() {
        return getSP().getString(AGE, DEFVALUE);
    }

    public boolean saveAge(String age) {
        return saveSP(AGE, age);
    }

    public String getFirstName() {
        return getSP().getString(FIRSTNAME, DEFVALUE);
    }

    public boolean saveFirstName(String firstname) {
        return saveSP(FIRSTNAME, firstname);
    }

    public String getLastName() {
        return getSP().getString(LASTNAME, DEFVALUE);
    }

    public boolean saveLastName(String lastname) {
        return saveSP(LASTNAME, lastname);
    }

    public String getEmail() {
        return getSP().getString(EMAIL, DEFVALUE);
    }

    public boolean saveEmail(String email) {
        return saveSP(EMAIL, email);
    }

    public Usuarios getUsuarios() {
        Usuarios s = new Usuarios();

        s.setId(getId());
        s.setFirstname(getFirstName());
        s.setLastname(getLastName());
        s.setEmail(getEmail());
        s.setIsConnected(false);

        return s;
    }

    public boolean saveIsConnected(boolean isConnected) {
        boolean bRetorno = true;
        try {
            SharedPreferences.Editor spEditor = getSP().edit();
            spEditor.putBoolean(ISCONNECTED, isConnected);
            spEditor.commit();
        }
        catch (Exception e) {
            bRetorno = false;
        }
        return bRetorno;
    }
}