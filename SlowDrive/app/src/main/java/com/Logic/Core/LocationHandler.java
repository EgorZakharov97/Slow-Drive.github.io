package com.Logic.Core;

import android.content.Context;
import android.location.Location;

import android.util.Log;


import com.Logic.Audio;
import com.Logic.Interface.LocationReceiver;
import com.Logic.Manager.HistoryManager;
import com.Logic.Object.LocationData;
import com.Logic.Object.CLocation;
import com.Logic.Static.State;
import com.UI.MainActivity;


public class LocationHandler {
    // CONSTANTS
    private static final String TAG = "LLocation Handler";

    //  VARIABLES
    private static boolean canRequest;
    private static HistoryManager mng;
    private static Context serviceContext;
    private static LocationReceiver subscriber;
    private static boolean shouldRequestSpeedLimit;
    private static boolean useSound;

    private static CLocation userLocation;

    public static void init(Context context){
        serviceContext = context;
        canRequest = true;
        mng = new HistoryManager();
    }

    public static void setRequestOrNot(boolean should){
        shouldRequestSpeedLimit = should;
    }

    public static void shouldUseSound(boolean should){
        useSound = should;
    }

    public static CLocation getLastLocation(){
        return userLocation;
    }

    public static void onLocationChanged(Location location){
        Log.d(TAG, "Current speed: " + location.getSpeed());
        if(location.getSpeed() != 0.0){
            if (shouldRequestSpeedLimit && canRequest) {
                // Make speed limit request
                try{
                    Request.makeRequest(location);
                    // Until the response is received, we cannot make another request
                    canRequest = false;
                } catch (Exception exception){
                    exception.printStackTrace();
                    // make sure we will handle the next request (not necessary on an empty value again)
                    canRequest = true;

                    if(subscriber != null){
                        subscriber.onLocationUpdated(new CLocation(location));
                    }
                }
            } else {
                State.setIsExceeding(false);
                if(subscriber != null){
                    subscriber.onLocationUpdated(new CLocation(location));
                }
            }
        }
    }

    public static void onRequestFinished(CLocation data){
        Log.d(TAG, "Current speed limit: " + data.getSpeedLimit());
        canRequest = true;
        userLocation = data;
        State.checkIfExceeding(userLocation);

        if(State.isExceeding()){
            if(useSound){
                Audio.play();
            }
            // save current speed to show it in history
            LocationData currentDataLocation = new LocationData(data);
            // save location information
            boolean ok;
            if(data.getSpeedLimit()<=0)
                ok= mng.add(data.getLongitude(), data.getLatitude(), MainActivity.DEFAULT_SPEED_LIMIT_MPH, data.getStreetName());
            else
                ok= mng.add(data.getLongitude(), data.getLatitude(), data.getSpeedLimit(), data.getStreetName());
            Log.e("length:",""+mng.getList().size()+" "+ok);
        }

        if(subscriber != null){
            subscriber.onLocationUpdated(userLocation);
        }
    }

    public static void subscribe(LocationReceiver receiver){
        subscriber = receiver;
    }
}
