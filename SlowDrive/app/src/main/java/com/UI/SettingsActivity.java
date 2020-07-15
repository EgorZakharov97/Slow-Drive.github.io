package com.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.ImageButton;
import android.widget.TextView;

import com.Logic.Object.CLocation;
import com.Logic.Core.LocationHandler;
import com.Logic.Core.LocationService;
import com.Logic.Interface.LocationReceiver;
import com.Logic.Static.Profile;
import com.Logic.Static.State;
import com.Logic.Utils.PasswordUtils;
import com.application.Services;
import com.Logic.Object.Driver;
import com.R;
import com.Database.Interface.IDriverDB;

public class SettingsActivity extends AppCompatActivity implements LocationReceiver {
    // CONSTANTS
    private final String TAG = "SETTINGS_ACTIVITY";
    public static final String SETTINGS_PREFS = "SETTINGS";
    public static final String USE_METERS = "USE_METERS";
    public static final String VOLUME = "VOLUME_LVL";
    public static final String USE_IN_BACKGROUND = "USE_IN_BACKGROUND";
    public static final String REQUEST_SPEED_LIMIT = "REQUEST_SPEED_LIMIT";
    public static final String USE_SOUND = "USE_SOUND";

    // UI VARIABLES
    private Switch sw_metric, sw_req_background, sw_speed_limit, sw_use_sound;
    private TextView tvs_speed, tvs_limit, tvs_header, tvl_header;
    private SeekBar volume_bar;
    private ImageButton back_settings;
    private Button password_submit, logout;
    private TextView username, password, email, profile, newPass, confirmNewPass, PasswordAuthentication;

    // VARIABLES
    private Driver driverInfo;
    private IDriverDB driverDataBase;
    private Intent serviceIntent;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Class level variables
        prefs = getSharedPreferences(SETTINGS_PREFS, MODE_PRIVATE);
        editor = prefs.edit();
        serviceIntent = new Intent(getApplicationContext(), LocationService.class);

        // UI components
        // settings
        sw_metric = findViewById(R.id.swch_metric);
        sw_req_background = findViewById(R.id.switch_req_background);
        sw_speed_limit = findViewById(R.id.sw_speed_limit);
        sw_use_sound = findViewById(R.id.sw_use_sound);
        volume_bar = findViewById(R.id.volume_bar);
        // speed screens
        tvs_speed = findViewById(R.id.tvs_speed);
        tvs_limit = findViewById(R.id.tvs_limit);
        tvs_header = findViewById(R.id.tvs_header);
        tvl_header = findViewById(R.id.tvl_header);
        // user
        username = findViewById(R.id.usernamePrompt);
        password = findViewById(R.id.currPassPrompt);
        newPass = findViewById(R.id.newPassPrompt);
        confirmNewPass = findViewById(R.id.confirmPassPrompt);
        PasswordAuthentication = findViewById(R.id.PasswordAuthentication);
        email = findViewById(R.id.emailPrompt);
        profile = findViewById(R.id.Profile_hint);
        logout = findViewById(R.id.logout);
        password_submit = findViewById(R.id.password_submit);
        // back button
        back_settings = findViewById(R.id.back_settings);

        LocationHandler.subscribe(this);

        // Grab the current logged in driver's settings information
        driverInfo = Profile.getCurrentDriver();

        // If there is no info, use default [GUEST account]
        boolean guest=false;
        if (driverInfo == null) {
            driverInfo = new Driver();
            guest=true;
        }

        // Grab the driver's database, encase we change their password
        driverDataBase = Services.getDriverDB();

        loadState();

        // Recommended not to perform concatenation with setText
        String fullText = driverInfo.getFirstName() + "'s Profile";
        profile.setText(fullText);
        username.setText(driverInfo.getUserName());
        if(!guest) {//not a guest
            email.setText(driverInfo.getEmail());
        }else{ //guest
            password_submit.setEnabled(false);
            password.setEnabled(false);
            newPass.setEnabled(false);
            confirmNewPass.setEnabled(false);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToThisScreen(SplashScreenActivity.class);
            }
        });

        password_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        back_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToThisScreen(MainActivity.class);
            }
        });

        volume_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                State.volume=progress;
                editor.putInt(VOLUME, progress);
                saveState();
            }
        });

        sw_metric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "User metrics switched; use meters:" + isChecked);
                editor.putBoolean(USE_METERS, isChecked);
                saveState();
                if (isChecked)
                    sw_metric.setText("Unit Preference: KPH ");
                else
                    sw_metric.setText("Unit Preference: MPH ");

            }
        });

        sw_req_background.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "Request in background switched; use service in background: " + isChecked);
                editor.putBoolean(USE_IN_BACKGROUND, isChecked);
                saveState();
            }
        });

        sw_metric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                State.metric=isChecked;
                Log.i(TAG, "User metrics switched; use meters:" + isChecked);
                editor.putBoolean(USE_METERS, isChecked);
                CLocation.setUseMeters(isChecked);
                saveState();
            }
        });

        sw_speed_limit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "Request in switched: Should request? -" + isChecked);
                State.showSpeedLimit=isChecked;
                editor.putBoolean(REQUEST_SPEED_LIMIT, isChecked);
                LocationHandler.setRequestOrNot(isChecked);
                saveState();
                manageSpeedLimitScreen();
            }
        });

        sw_use_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                State.noSound=!isChecked;
                Log.i(TAG, "Use sound changed: Use sound? -" + isChecked);
                editor.putBoolean(USE_SOUND, isChecked);
                LocationHandler.shouldUseSound(isChecked);
                saveState();
            }
        });
    }

    @Override
    public void onLocationUpdated(CLocation location) {
        if(location != null){
            tvs_speed.setText(String.valueOf(location.getDisplaySpeed()));
            if(location.hasAllData()){
                tvs_limit.setText(String.valueOf(location.getSpeedLimit()));
            }
        }
    }

    private void manageSpeedLimitScreen(){
        CLocation lastLocation = LocationHandler.getLastLocation();
        if(lastLocation != null){
            enableSpeedScreen();
            tvs_speed.setText(String.valueOf(lastLocation.getDisplaySpeed()));
            if(prefs.getBoolean(REQUEST_SPEED_LIMIT, true) && lastLocation.hasAllData()){
                enableLimitScreen();
                tvs_limit.setText(String.valueOf(lastLocation.getSpeedLimit()));
            } else {
                disableLimitScreen();
            }
        } else {
            disableLimitScreen();
            disableSpeedScreen();
        }
    }

    private void disableSpeedScreen(){
        tvs_speed.setVisibility(View.INVISIBLE);
        tvs_header.setVisibility(View.INVISIBLE);
    }

    private void enableSpeedScreen(){
        tvs_speed.setVisibility(View.VISIBLE);
        tvs_header.setVisibility(View.VISIBLE);
    }

    private void disableLimitScreen(){
        tvs_limit.setVisibility(View.INVISIBLE);
        tvl_header.setVisibility(View.INVISIBLE);
    }

    private void enableLimitScreen(){
        tvs_limit.setVisibility(View.VISIBLE);
        tvl_header.setVisibility(View.VISIBLE);
    }

    private void changePassword(){

        String securePass = driverInfo.getPassword();
        String cPass = password.getText().toString();
        String nPass = newPass.getText().toString();
        String id = driverInfo.getUserName();

        System.out.print("SecurePass: " + securePass);
        System.out.print("cPass: " + cPass);

        boolean match = PasswordUtils.verifyUserPassword(cPass, securePass, driverDataBase.getDriverSalt(id));


            if (nPass.equals("")) {
                PasswordAuthentication.setText("Please add your new password into the field.");
            }
            else
            {
                if (confirmNewPass.getText().toString().equals("")) {
                    PasswordAuthentication.setText("Please confirm your password.");
                }
                else{
                    if (!nPass.equals(confirmNewPass.getText().toString())) {
                        // Apply 'fields are not equal prompt'
                        PasswordAuthentication.setText("Passwords are not equal. Please confirm the new password.");
                    } else {

                        if(!match){
                            PasswordAuthentication.setText("Incorrect current password.");
                        } else {

                           // Change password in database
                            driverDataBase.changePassword(nPass, driverInfo);

                            // Apply 'Updated password prompt'
                            PasswordAuthentication.setText("Password has been updated!");
                            this.newPass.setText("");
                            this.confirmNewPass.setText("");
                        }

                    }
                }
            }



    }

    @Override
    protected void onStop() {
        super.onStop();
        saveState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause called");
        if(!prefs.getBoolean(USE_IN_BACKGROUND, false)){
            stopService(serviceIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadState();
        if(MainActivity.wasServiceTurnedOn() && !LocationService.isServiceRunning()){
            startService(serviceIntent);
            LocationHandler.subscribe((LocationReceiver) this);
        }
        manageSpeedLimitScreen();
        loadState();
    }

    private void saveState(){
        editor.apply();
        Log.i(TAG, "Settings were updated");
    }

    private void loadState(){
        sw_metric.setChecked(prefs.getBoolean(USE_METERS, false));
        sw_req_background.setChecked(prefs.getBoolean(USE_IN_BACKGROUND, false));
        sw_speed_limit.setChecked(prefs.getBoolean(REQUEST_SPEED_LIMIT, true));
        sw_use_sound.setChecked(prefs.getBoolean(USE_SOUND, true));
        volume_bar.setProgress(prefs.getInt(VOLUME, 80));
        State.metric=sw_metric.isChecked();
        State.noSound=!sw_use_sound.isChecked();
        State.volume=80;
        State.showSpeedLimit=sw_speed_limit.isChecked();

    }

    private void goToThisScreen(Class c){
        startActivity(new Intent(this, c));
    }
}
