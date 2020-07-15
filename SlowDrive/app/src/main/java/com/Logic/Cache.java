package com.Logic;

import com.Logic.Interface.ILocationCache;
import com.Logic.Object.LocationData;
import com.Logic.Static.Calculation;

//Database interface implementation
//will move logic method in iteration 2
public class Cache implements ILocationCache {
    private int size ;
    private LocationData[] cache;
    private int[] numUsed;
    private int counter;
    public Cache(int size) {
        this.size=size;
        cache = new LocationData[size];
        for (int i = 0; i < size; i++) {
            cache[i] = null;
        }
        numUsed = new int[size];
        counter = 0;
    }
    @Override
    public boolean empty(){
        return counter==0;
    }

    @Override
    public boolean add(LocationData x) {//auto replace an item when cache is full(no delete required)
        boolean ret=false;
        if (x!=null&&valid(x)&&!search(x)) { //checks duplication
            if (counter < 20) {
                cache[counter] = x;
                counter++;
            } else { //replace a location that is least used
                int curr = numUsed[0];
                int pos = 0;
                for (int i = 1; i < counter; i++) { //O(n), optimization possible
                    if (numUsed[i] <= curr) {
                        curr = numUsed[i];
                        pos = i;
                    }
                }
                ret=true;
                cache[pos] = x;
                numUsed[pos] = 0;
            }
        }
        return ret;
    }
    @Override
    public LocationData findNearest(LocationData x) {
        LocationData ret = null;
        int pos = -1;
        if(x!=null&&valid(x)&&!empty()) {
            double currDistance = Calculation.calcDistance(x, cache[0]);
            ret = cache[0];
            pos=0;
            for (int i = 1; i < counter; i++) { //O(n), optimization possible
                double distance = Calculation.calcDistance(x, cache[i]);
                if (distance < currDistance) {
                    currDistance = distance;
                    ret = cache[i];
                    pos = i;
                }
            }
        }
        if(pos!=-1)
            numUsed[pos]+=1;
        return ret;
    }
    @Override
    public LocationData findNearest(LocationData x,int offset) {
        LocationData nearest=findNearest(x);
        if(nearest!=null&&Calculation.calcDistance(x,nearest)>offset){
            int index= getIndex(nearest);
            nearest=null;
            numUsed[index]-=1;
        }
        return nearest;
    }

    @Override
    public int numElements ()
    {
        return this.counter;
    }

    @Override
    public int getMaxSize ()
    {
        return this.size;
    }

    //helper methods
    public boolean search(LocationData x) {
        boolean flag = false;
        if (!empty()) {
            for (int i = 0; i < counter && !flag; i++) {
                if (x.latitude == cache[i].latitude
                        && x.longitude == cache[i].longitude
                ) {
                    flag = true;
                }
            }
        }
        return flag;

    }



    private int getIndex(LocationData x){
        int index=-1;
        boolean stop=false;
        for(int i=0;i<counter&&!stop;i++){
            if(x.longitude==cache[i].longitude&&x.latitude==cache[i].latitude){
                index=i;
                stop=true;
            }
        }
        return index;
    }
    private boolean valid(LocationData x){
        return !(x.longitude < -180 || x.longitude > 180 ||
                x.latitude < -180 / 2 || x.latitude > 180 / 2 || x.speedLimit < 0);
    }
}
