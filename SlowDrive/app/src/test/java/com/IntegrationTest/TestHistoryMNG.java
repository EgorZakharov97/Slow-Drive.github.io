package com.IntegrationTest;

import com.Database.HistoryDB;
import com.Logic.Object.LocationData;
import com.Logic.Manager.HistoryManager;
import com.application.Main;
import com.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestHistoryMNG {
    private HistoryDB db;
    private File dbFile;
    private HistoryManager mng;

    LocationData pembina = new LocationData (-97.151693, 49.828832, 60,true,"Pembina");
    LocationData theAvenue= new LocationData(-6.796006,53.169255,20,true,"The Avenue");


    @Before
    public void setup() throws IOException {
        dbFile= TestUtils.copyDB();
        db=new HistoryDB(Main.getDBPathName());
        mng=new HistoryManager(db);
    }
    @Test
    public void testAdd(){
        //invalid position
        for(int i=-180;i<=-200;i--){
            assertEquals(mng.add(i,i,60,""+i),false);
        }

        //valid position
        assertEquals(mng.add(pembina.getLongitude(),pembina.getLatitude(),pembina.getSpeedLimit(),"pembina"),true);
        //did it pass to database
        ArrayList arr= mng.getList();
        assertEquals(arr.size(),1);
        //do duplication
        assertEquals(mng.add(pembina.getLongitude(),pembina.getLatitude(),pembina.getSpeedLimit(),"pembina"),false);
    }
    @Test
    public void testList(){
        //test no element
        ArrayList<LocationData> arr=mng.getList();
        assertEquals(arr.size(),0);
        assertEquals(mng.add(pembina.getLongitude(),pembina.getLatitude(),pembina.getSpeedLimit(),"Pembina"),true);
        //did it pass to database
         arr= mng.getList();
        assertEquals(arr.size(),1);
        //element correct?
        LocationData data= arr.get(0);
        assert(data.getStreetName().equals(pembina.getStreetName()));
        assert(pembina.getLatitude()==data.getLatitude());
        assert(pembina.getLongitude()==data.getLongitude());


    }
    @Test
    public void testRemove(){
        //remove from nothing
        assert(!(mng.remove("Pembina")));
        ArrayList<LocationData> local= new ArrayList<>();
        //add some valid position
        mng.add(pembina.getLongitude(),pembina.getLatitude(),60,pembina.getStreetName());
        mng.add(theAvenue.getLongitude(),theAvenue.getLatitude(),60,theAvenue.getStreetName());

        assertEquals(mng.remove("Pembina"),true);
        assertEquals(mng.remove("The Avenue"),true);
        assertEquals(mng.getList().size(),0);
    }
    @Test
    public void testWarn(){
        //check null
        assert(!mng.haveOverSpeed("Pembina"));

        mng.add(pembina.getLongitude(),pembina.getLatitude(),60,pembina.getStreetName());
        mng.add(theAvenue.getLongitude(),theAvenue.getLatitude(),60,theAvenue.getStreetName());
        //check valid
        assert(mng.haveOverSpeed("Pembina"));
        assert(mng.haveOverSpeed("The Avenue"));

        //check invalid
        assert(!mng.haveOverSpeed("University"));
    }
    @After
    public void clean(){
        dbFile.delete();
    }
}
