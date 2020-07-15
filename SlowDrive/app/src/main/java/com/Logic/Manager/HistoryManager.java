package com.Logic.Manager;

import com.Database.Interface.ILocationHistoryDB;
import com.Logic.Interface.IHistoryMng;
import com.Logic.Object.LocationData;
import com.application.Services;

import java.util.ArrayList;

public class HistoryManager implements IHistoryMng {

    private ILocationHistoryDB dataList;
    public HistoryManager( ){

        dataList = Services.getHistoryDB();;
    }
    public HistoryManager(ILocationHistoryDB database){
        dataList=database;
    }
    public boolean add(double longitude,double latitude,int speedLimit,String streetName){
        boolean ret = true;

        if(streetName!=null) { //passed validation, safe to use handler

            if(streetName.length()>0) {
                LocationData data = new LocationData(longitude, latitude, speedLimit, streetName); //will not add a location even there's no street name
                boolean ext = dataList.existsLocation(streetName);
                if (ext) {
                    ret = false;
                } else {
                    dataList.addLocation(data);
                }
            }else{
                ret=false;
            }
        }
        else{
            ret=false;
        }
        return ret;
    }
    //Remove
    //outPut: true= successfully removed
    //        false= failed to remove
    public boolean remove(String streetName){
        boolean ret;
        if(streetName!=null) {
            if (streetName.length() > 0) {//got some street name
                ret = dataList.existsLocation(streetName);
                if (ret) {//safe to remove
                    dataList.deleteLocation(streetName);
                } else {
                    ret = false;
                }
            } else { //some place without name
                ret = false;
            }
        }else{
            ret=false;
        }
        return ret;
    }

    public ArrayList<LocationData> getList(){
        return dataList.getList();
    }
    public boolean haveOverSpeed(String street){
        boolean find=false;
        ArrayList<LocationData> list=getList();
        for(int i=0;i<list.size()&&!find;i++){
            if(list.get(i).getStreetName().equals(street)){
                find=true;
            }
        }
        return find;
    }

}
