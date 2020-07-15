package com.Logic.Static;

import com.Logic.Object.LocationData;

public class Calculation {
    //this method calculates distance between two locations(lat, long) in meters
    private static final int CONVERSION=6371*1000;
    private static final int DEGREE=180;
    public static int calcDistance(LocationData scr, LocationData des){
        double dLat=degToRad(des.latitude-scr.latitude);
        double dLon=degToRad(des.longitude-scr.longitude);
        double a = Math.sin(dLat/2.) * Math.sin(dLat/2.) + Math.sin(dLon/2.) * Math.sin(dLon/2.) * Math.cos(degToRad(scr.latitude)) * Math.cos(degToRad(des.latitude));
        double c=2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dis=c*CONVERSION;
        return (int)dis;
    }
    //Returns value in meters
    public static double degToRad(double degree){
        return degree * Math.PI / DEGREE;
    }
}
