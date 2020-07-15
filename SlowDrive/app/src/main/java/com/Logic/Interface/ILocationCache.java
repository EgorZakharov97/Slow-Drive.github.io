package com.Logic.Interface;
import com.Logic.Object.LocationData;

//database interface
public interface ILocationCache {
    boolean add(LocationData x); //add data to cache and auto replace when full

    boolean empty(); //checks if the cache is empty

    /*find data that is closest to a given location
    /can also provide a maximum distance between two point in meters
    */
    int numElements();

    int getMaxSize();

    LocationData findNearest(LocationData x);

    LocationData findNearest(LocationData x, int offset);

}