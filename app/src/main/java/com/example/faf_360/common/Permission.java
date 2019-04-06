package com.example.faf_360.common;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class Permission {

    public static final int LOCATION_REQUEST = 10;
    public static final int STORAGE_REQUEST = 11;
    public static final int VIBRATOR_REQUEST = 12;

    public static boolean Location(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Permission.LOCATION_REQUEST);
            return false;
        }
        return true;
    }

    public static boolean Storage(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permission.STORAGE_REQUEST);
            return false;
        }
        return true;
    }

    public static boolean Vibrator(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.VIBRATE}, Permission.VIBRATOR_REQUEST);
            return false;
        }
        return true;
    }

}
