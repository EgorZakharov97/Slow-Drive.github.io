package com.Logic.Object;

import android.location.Location;

public class CLocation {
    private final Location location;
    private static boolean useMeters;
    private boolean hasAllData;
    private int speedLimitMPH;
    private String streetName;

    //constant
    private final float mphToKMH_CONVERSION= 1.60934f;
    private final float msToKMH_CONVERSION=3.6f;
    private final float msToMPH_CONVERSION=2.23694f;
    private final double altitude_CONVERSION=3.2808398d;
    public CLocation(Location location, int speedLimitMPH, String streetName){
        this.location = location;
        this.speedLimitMPH = speedLimitMPH;
        this.streetName = streetName;
        hasAllData = true;
    }

    public CLocation(Location location){
        this.location = location;
        hasAllData = false;
    }

    //--------
    // SETTERS
    //--------

    public static void setUseMeters(boolean bool){
        useMeters = bool;
    }

    //--------
    // GETTERS
    //--------

    public String getStreetName(){
        return streetName;
    }

    public int getSpeedLimit(){

        if(useMeters){
            int speedLimitKMH = (int)(speedLimitMPH * mphToKMH_CONVERSION);
            int x = speedLimitKMH % 5 == 0 ? 0 : 1;
            speedLimitKMH = ((speedLimitKMH/5) + x) * 5;
            return speedLimitKMH;
        } else {
            return speedLimitMPH;
        }
    }

    public int getDisplaySpeed(){
        if(useMeters){
            return (int)(location.getSpeed() * msToKMH_CONVERSION); // m/s to km/hr
        } else {
            return (int) (location.getSpeed() * msToMPH_CONVERSION); // m/s to miles/h
        }
    }

    public double getAltitude() {
        double nAltitude = location.getAltitude();

        if(!useMeters){
//            Convert metrics to feet
            nAltitude = nAltitude * altitude_CONVERSION;
        }

        return nAltitude;
    }

    public float getSpeed() { // returns in miles/h
        return location.getSpeed() * msToMPH_CONVERSION;
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public boolean hasAllData(){
        return hasAllData;
    }

}
