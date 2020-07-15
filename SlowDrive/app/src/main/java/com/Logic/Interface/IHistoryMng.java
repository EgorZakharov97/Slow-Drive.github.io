package com.Logic.Interface;

import com.Logic.Object.LocationData;

import java.util.ArrayList;

public interface IHistoryMng {
    boolean add(double longitude,double latitude,int speedLimit,String streetName);
    boolean remove(String streetName);
    ArrayList<LocationData> getList();
}
