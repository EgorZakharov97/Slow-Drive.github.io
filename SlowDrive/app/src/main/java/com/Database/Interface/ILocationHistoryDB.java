package com.Database.Interface;

//I assume that locations are going to be stored in the same order like they've been added

import com.Logic.Object.LocationData;

import java.util.ArrayList;

public interface ILocationHistoryDB {
    boolean addLocation(LocationData location);
    ArrayList<LocationData> getList();
    void deleteLocation(String streetName);
    boolean existsLocation(LocationData location);
    boolean existsLocation(String streetName);
}