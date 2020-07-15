package com.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import com.Logic.*;
import com.Logic.Object.CLocation;
import com.Logic.Core.LocationHandler;
import com.Logic.Core.LocationService;
import com.Logic.Manager.HistoryManager;
import com.Logic.Static.State;
import com.R;


public class MainActivity extends AppCompatActivity implements com.Logic.Interface.LocationReceiver {

    //  CONSTANTS
    private static final String TAG = "Activity_MAIN";
    public static final int DEFAULT_SPEED_LIMIT_KMH = 40;
    public static final int DEFAULT_SPEED_LIMIT_MPH = 30;

    private static final int speedLimit=50;
    //  UI COMPONENTS
    private TextView tv_speed, curr_speed_limit,warn,speed_limit_unit;
    private ImageView limit_view;
    private Button btn_start;
    private ImageButton settings_button;


    // VARIABLES
    private SharedPreferences prefs;
    private boolean paused;
    private HistoryManager mng;
    private boolean shouldSpeedLimit;
    private static boolean serviceTurnedOn;
    private Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paused = false;
        Log.d(TAG, "onCreate Called");

//      Setting UI components
        tv_speed = findViewById(R.id.tv_speed);
        curr_speed_limit = findViewById(R.id.curr_speed_limit);
        warn = findViewById(R.id.warn);
        speed_limit_unit = findViewById(R.id.speed_limit_unit);
        settings_button = findViewById(R.id.btn_settings);
        btn_start = findViewById(R.id.btn_start);

        if(serviceTurnedOn) {
            btn_start.setBackgroundColor(getResources().getColor(R.color.red));
            btn_start.setText(R.string.btn_disable);
        } else {
            btn_start.setBackgroundColor(getResources().getColor(R.color.green));
            btn_start.setText(R.string.btn_enable);
        }
        limit_view = findViewById(R.id.img_speed_limit);

        // VARIABLES
        prefs = getSharedPreferences(SettingsActivity.SETTINGS_PREFS, MODE_PRIVATE);
        State.metric=prefs.getBoolean(SettingsActivity.USE_METERS, false);
        State.showSpeedLimit= prefs.getBoolean(SettingsActivity.REQUEST_SPEED_LIMIT,true);
        CLocation.setUseMeters(prefs.getBoolean(SettingsActivity.USE_METERS, false));
        mng = new HistoryManager();
        shouldSpeedLimit = prefs.getBoolean(SettingsActivity.REQUEST_SPEED_LIMIT, true);

        serviceIntent = new Intent(getApplicationContext(), LocationService.class);

        // Initial settings
        Audio.setupAudio(getApplicationContext(), (AudioManager) getSystemService(Context.AUDIO_SERVICE));
        LocationHandler.setRequestOrNot(prefs.getBoolean(SettingsActivity.REQUEST_SPEED_LIMIT, true));
        LocationHandler.shouldUseSound(prefs.getBoolean(SettingsActivity.USE_SOUND, true));


        setDisplayedUnits();

        if (!runtimePermissions()){
            Log.d(TAG, "Start button enabled");
            enableStartButton();
        }

        //==============================
        // Event Listeners
        //==============================

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToThisScreen(SettingsActivity.class);
            }
        });


    }

    private void setDisplayedUnits() {
        if(useMeters()){
            speed_limit_unit.setText("KPH");
        } else {
            speed_limit_unit.setText("MPH");
        }
    }

    public static boolean wasServiceTurnedOn(){
        return serviceTurnedOn;
    }

    private boolean useMeters() {
        return prefs.getBoolean(SettingsActivity.USE_METERS, false);
    }

    private boolean useInBackground(){
        return prefs.getBoolean(SettingsActivity.USE_IN_BACKGROUND, false);
    }

    private void enableStartButton() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serviceTurnedOn){
                    stopService(serviceIntent);
                    btn_start.setText(R.string.btn_enable);
                    btn_start.setBackgroundColor(getResources().getColor(R.color.green));
                    updateCurrentSpeedToUnavailable();
                    updateSpeedLimitToUnavailable();
                    serviceTurnedOn = false;
                } else {
                    startService(serviceIntent);
                    LocationHandler.subscribe(MainActivity.this);
                    btn_start.setText(R.string.btn_disable);
                    btn_start.setBackgroundColor(getResources().getColor(R.color.red));
                    serviceTurnedOn = true;
                }
            }
        });
    }

    private boolean runtimePermissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    // Call this method with the class that is tied to a layout/~.xml file
    // Check other UI classes for reference
    private void goToThisScreen(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart Called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop Called");

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!prefs.getBoolean(SettingsActivity.USE_IN_BACKGROUND, false)){
            stopService(serviceIntent);
        }
        Log.d(TAG, "onPause Called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume Called");
        LocationHandler.subscribe(this);
        CLocation lastLocation = LocationHandler.getLastLocation();
        shouldSpeedLimit = prefs.getBoolean(SettingsActivity.REQUEST_SPEED_LIMIT, true);

        if(serviceTurnedOn && !LocationService.isServiceRunning()){
            startService(serviceIntent);
            LocationHandler.subscribe(this);
        }

        if(lastLocation != null){
            updateCurrentSpeed(lastLocation);

            if(lastLocation.hasAllData()){
                updateSpeedLimit(lastLocation);
            }
        } else {
            updateCurrentSpeedToUnavailable();
            updateSpeedLimitToUnavailable();
        }

        if(prefs.getBoolean(SettingsActivity.REQUEST_SPEED_LIMIT, true)){
            State.showSpeedLimit=true;
            enableSpeedLimitScreen();
        } else {
            State.showSpeedLimit=false;
            disableSpeedLimitScreen();
        }

        setDisplayedUnits();
    }

    private void enableSpeedLimitScreen() {
        limit_view.setVisibility(View.VISIBLE);
        curr_speed_limit.setVisibility(View.VISIBLE);
        speed_limit_unit.setVisibility(View.VISIBLE);
    }

    private void disableSpeedLimitScreen() {
        limit_view.setVisibility(View.INVISIBLE);
        curr_speed_limit.setVisibility(View.INVISIBLE);
        speed_limit_unit.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy Called");
        stopService(serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enableStartButton();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onLocationUpdated(CLocation location) {

        // update speed and speed limit
        updateCurrentSpeed(location);
        if(shouldSpeedLimit && location.hasAllData()){
            updateSpeedLimit(location);
        } else if(!shouldSpeedLimit){
            updateSpeedLimitToUnavailable();
        }

        // if over the speed limit
         if(State.isExceeding()){

            // set the color of speedLimit to red
            tv_speed.setTextColor(getResources().getColor(R.color.red));
        } else {
            // bring the color back to normal
            tv_speed.setTextColor(getResources().getColor(R.color.black_overlay));
        }

        if (mng.haveOverSpeed(location.getStreetName())) {
            State.warn=true;
            warn.setTextColor(getResources().getColor(R.color.warning));
            warn.setText("Be careful. You've oversped here in the past.");
        } else {
            State.warn=false;
            warn.setTextColor(getResources().getColor(R.color.black_overlay));
            warn.setText("");
        }
    }

    private void updateSpeedLimitToUnavailable() {
        curr_speed_limit.setText("...");
        warn.setVisibility(View.INVISIBLE);
        Log.i(TAG, "Speed limit unavailable!");
    }

    private void updateCurrentSpeedToUnavailable() {
        tv_speed.setText("...");
        Log.i(TAG, "Speed unavailable!");
    }

    private void updateCurrentSpeed(CLocation location) {
        int speed = location == null ? 0 : location.getDisplaySpeed();


        if (useMeters()) {
            tv_speed.setText(speed + " km/h");
        } else {
            tv_speed.setText(speed + " mile/h");
        }
        Log.i(TAG, "Current speed: " + speed);
    }

    private void updateSpeedLimit(CLocation locationData) {
        warn.setVisibility(View.VISIBLE);
        if (locationData != null && locationData.getSpeedLimit() > 0) {
            String speedLimit = String.valueOf(locationData.getSpeedLimit());
            if (useMeters()) {
                curr_speed_limit.setText(speedLimit);
            } else {
                curr_speed_limit.setText(speedLimit);
            }
            Log.i(TAG, "Current speed limit updated: " + locationData.getSpeedLimit());
        } else {
            // Use default limits
            if (useMeters()) {
                curr_speed_limit.setText(String.valueOf(DEFAULT_SPEED_LIMIT_KMH));
            } else {
                curr_speed_limit.setText(String.valueOf(DEFAULT_SPEED_LIMIT_KMH));
            }
            Log.i(TAG, "Default speed limit (data location contains speedLimit=0)");
        }
    }
}