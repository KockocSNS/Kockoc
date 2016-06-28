package com.kocapplication.pixeleye.kockocapp.util.map;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by Han_ on 2016-06-27.
 */
public class GpsInfo extends Service implements LocationListener {
    private final String TAG = "GPS_INFORMATION";
    private Context context;

    private Location location;
    private double latitude;    //위도
    private double longitude;   //경도

    private boolean isGPSEnable = false;
    private boolean isNetworkEnable = false;
    private boolean isGetLocation = false;

    private LocationManager locationManager;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;

    public GpsInfo(Context context) {
        super();
        this.context = context;

        getLocation();
    }

    public Location getLocation() {

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.i(TAG, isGPSEnable + "");
        Log.i(TAG, isNetworkEnable + "");

        try {
            if (isGPSEnable)
                location = getLocationFromGPS(location);

            else if (isNetworkEnable)
                location = getLocationFromNetwork(location);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG, "SECURITY EXCEPTION");
        }

        return location;
    }

    private Location getLocationFromNetwork(Location location) throws SecurityException {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        if (locationManager == null) return null;

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        isGetLocation = true;

        return location;
    }

    private Location getLocationFromGPS(Location location) throws SecurityException {
        if (location != null) return location;

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        if (locationManager == null) return null;

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        isGetLocation = true;

        return location;
    }

    public double getLatitude() {
        if (location != null) latitude = location.getLatitude();
        return latitude;
    }

    public double getLongitude() {
        if (location != null) longitude = location.getLongitude();
        return longitude;
    }

    public boolean isGetLocation() {
        return isGetLocation;
    }

    public void showSettingAlertDialog() {
        DialogInterface.OnClickListener listener = new DialogButtonListener();

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("GPS 사용 유무")
                .setMessage("GPS 세팅이 되어있지 않습니다.\n설정창으로 가시겠습니까?")
                .setPositiveButton("확인", listener)
                .setNegativeButton("아니오", listener)
                .create();

        dialog.show();
    }

    private class DialogButtonListener implements Dialog.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == AlertDialog.BUTTON_POSITIVE) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            } else if (which == AlertDialog.BUTTON_NEGATIVE) {
                dialog.cancel();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
