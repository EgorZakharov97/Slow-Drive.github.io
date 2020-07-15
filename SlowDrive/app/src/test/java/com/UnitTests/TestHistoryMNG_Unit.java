package com.UnitTests;


import com.Database.Interface.ILocationHistoryDB;
import com.Logic.Manager.HistoryManager;
import com.Logic.Object.LocationData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestHistoryMNG_Unit {
    HistoryManager mng;
    @Mock
    private ILocationHistoryDB d =mock(ILocationHistoryDB.class);
    @Before
    public void setUp(){
        mng= new HistoryManager(d);
    }
    @Test
    public void testAlreadyExits(){
        when(d.existsLocation("street")).thenReturn(true);
        assertFalse(mng.add(1,1,1,"street"));
    }
    @Test
    public void invalidStreetName(){
        when(d.existsLocation("")).thenReturn(false);
        assertFalse(mng.add(1,1,1,""));

    }
    @Test
    public void validAdd(){
        when(d.existsLocation("street")).thenReturn(false);
        assertTrue(mng.add(1,1,1,"street"));
    }

    @Test
    public void remove(){
        //not in database
        when(d.existsLocation("street")).thenReturn(false);
        assertFalse(mng.remove("street"));

        //valid
        when(d.existsLocation("street")).thenReturn(true);
        assertTrue(mng.remove("street"));
    }
    @Test
    public void overSpeed(){
        //add one element
        mng.add(1,1,1,"street");
        ArrayList<LocationData> a= new ArrayList<LocationData>();
        a.add(new LocationData(1,1,1,"street"));
        when(d.getList()).thenReturn(a);
        //not found
        assertFalse(mng.haveOverSpeed("Pembina"));
        //find location
        assertTrue(mng.haveOverSpeed("street"));
    }
}
