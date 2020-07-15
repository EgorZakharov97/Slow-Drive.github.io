package com.Logic.Object;

//basic location data class
public class LocationData {
    //longitude and latitude should be in DD(decimal degree) format
    public double longitude,latitude;
    // speed limit should be stored in MPH
    public int speedLimit;
    // contains user's speed to show in history (not every instance of LocationData will have speed initialized)
    public float speed;
    public String streetName;



    public LocationData(CLocation dataLocation){

        latitude = dataLocation.getLatitude();
        longitude = dataLocation.getLongitude();
        speedLimit = dataLocation.getSpeedLimit();
        speed = dataLocation.getSpeed();
        streetName = dataLocation.getStreetName();
    }

    public LocationData(double longitude, double latitude, int speedLimit, String streetName){
        this.latitude = latitude;
        this.longitude = longitude;
        this.speedLimit = speedLimit;
        this.streetName = streetName;
    }

    public LocationData(double v, double v1, int i, boolean b, String streetName) {
        this.latitude = v1;
        this.longitude = v;
        this.speedLimit = i;
        this.streetName = streetName;

    }

    public float getSpeed(){
        return speed;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public String getStreetName() {
        return streetName;
    }
}