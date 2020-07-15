package com.UnitTests;

import com.Database.HistoryDB;
import com.Logic.Object.LocationData;
import com.application.Main;
import com.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestHistory {
    private HistoryDB db;
    private File dbFile;
    private int numTest=1;
    private ArrayList<LocationData> added;
    @Before
    public void setup() throws IOException {
        added= new ArrayList<>();
        dbFile=TestUtils.copyDB();
        db= new HistoryDB(dbFile.getAbsolutePath().replace(".script",""));
    }
    //also test some exists case
    @Test
    public void Add(){
        //set up
        HistoryDB db=new HistoryDB(Main.getDBPathName());
        for(int i=0;i<numTest;i++) {
            LocationData fake= new LocationData(i,i,i,true,"\""+i+"\"");
            db.addLocation(fake);
            added.add(fake);
            boolean ret = db.existsLocation(fake);
            assert (ret);

        }
    }
    //test if the array in database is valid
    @Test
    public void testCopy(){
        //set up
        HistoryDB db=new HistoryDB(Main.getDBPathName());

        for(int i=0;i<numTest;i++){
            LocationData fake= new LocationData(i,i,i,true,"\""+i+"\"");
            db.addLocation(fake);
            added.add(fake);
        }
        ArrayList<LocationData> compare= db.getList();
        assertEquals(added.size(),compare.size());
        for(int i=0;i<numTest;i++){
            assertEquals(compare.get(i).getStreetName(),added.get(i).getStreetName());
        }

    }

    @Test
    public void testRemove(){
       //set up
        HistoryDB db=new HistoryDB(Main.getDBPathName());
        for(int i=0;i<numTest;i++){
            LocationData fake= new LocationData(i,i,i,true,"\""+i+"\"");
            db.addLocation(fake);
            added.add(fake);
        }
        //delete elements in db
        for(int i=0;i<numTest;i++){
           db.deleteLocation(added.get(numTest-i-1).getStreetName());
        }
        assertEquals(db.getList().size(),0);
        //delete element not in db
        db.addLocation(new LocationData(1,1,1,true,"COMP 3350"));
        int pre=db.getList().size();
        db.deleteLocation("here is not COMP 3350 room");
        int curr=db.getList().size();
        assertEquals(pre, curr);
    }
    @Test
    public void testNotExist(){
        boolean where=db.existsLocation("COMP 3350");
        assertEquals(where,false);
    }

    @After
    public void clean(){
        dbFile.delete();
        added.clear();
    }
}
