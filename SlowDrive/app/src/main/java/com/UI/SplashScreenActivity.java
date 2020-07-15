package com.UI;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.Logic.Static.Profile;
import com.Logic.Core.Request;
import com.application.Main;
import static com.Logic.Static.State.serviceTurnedOn;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import com.R;

public class SplashScreenActivity extends AppCompatActivity {

    private Button login_splash, signup_splash, temp_button_to_main;
    private static boolean create=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        serviceTurnedOn=false;
        if(!create) {
            copyDatabaseToDevice();
            create=true;
        }
        try {
            ApplicationInfo app = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            Request.setAPI(bundle.getString("api_url"), bundle.getString("api_key"));
        } catch (Exception e) {
            Log.e("Missing metaData","Please check if Mainfest contains valid key and url. ");
        }
        signup_splash = findViewById(R.id.signup_splash);
        temp_button_to_main = findViewById(R.id.temp_button_to_main);
        login_splash = findViewById(R.id.login_splash);

        //==============================
        // Event Listeners
        //==============================

        temp_button_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Profile.updateDriver(null);
                goToThisScreen(MainActivity.class);
            }
        });

        signup_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToThisScreen(SignupActivity.class);
            }
        });

        login_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToThisScreen(LoginActivity.class);
            }
        });
    }

    private void goToThisScreen(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {

            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(assetNames, dataDirectory);

            Main.setDBPathName(dataDirectory.toString() + "/" + Main.getDBPathName());

        } catch (final IOException ioe) {
            String x="Unable to access application data: " + ioe.getMessage();
            Log.e("Error",x);
        }
    }

    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }
}
