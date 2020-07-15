package com.Logic.Core;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;



public class LocationService extends Service {

    private static final String TAG = "Location Service";
    private static LocationListener listener;
    private static LocationManager manager;

    public static boolean isServiceRunning(){
        return manager != null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "OnBind called");
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service was started");
        LocationHandler.init(this);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LocationHandler.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                // send user to settings
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500, 0, listener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service was disabled");
        super.onDestroy();
        if(manager != null){
            manager.removeUpdates(listener);
            manager = null;
        }
    }
}