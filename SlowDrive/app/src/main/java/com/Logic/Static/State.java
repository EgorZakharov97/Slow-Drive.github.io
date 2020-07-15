package com.Logic.Static;

import android.util.Log;
import com.Logic.Object.CLocation;


public class State {
    private static final String TAG = "STATE";
    private static final double conversion=1.609;
    private static boolean isExceeding;
    //For System Test purposes
    public static boolean serviceTurnedOn=false;
    public static boolean metric,noSound,showSpeedLimit,warn;
    public static int volume;
    //check if Driver is over Speeding
    public static void checkIfExceeding(CLocation location) {
        if(location.hasAllData()){
            int userSpeed = location.getDisplaySpeed();
            int speedLimit = location.getSpeedLimit();
            speedLimit = speedLimit <= 0 ? 50 : speedLimit + 5;

            isExceeding = userSpeed > speedLimit;

            if(isExceeding){
                Log.i(TAG, "User is exceeding the speed limit");
            }
        }
    }

    public static void setIsExceeding(boolean exceeding){
        isExceeding = exceeding;
    }

    public static boolean isExceeding(){
        return isExceeding;
    }
}
