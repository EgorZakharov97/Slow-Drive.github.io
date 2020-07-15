package com.UnitTests;

import android.location.Location;

import com.Logic.Object.CLocation;
import com.Logic.Object.LocationData;

import static junit.framework.TestCase.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestLocationData {

    LocationData location;

    @Before
    public void before(){
        location = new LocationData(10, 10, 40, "street");
    }

    @Test
    public void testGetSpeedLimit(){
        assertEquals(40, location.getSpeedLimit());
    }

    @Test
    public void testGetLongitude(){
        assertEquals(10.0, location.getLongitude());
    }

    @Test
    public void testGetLatitude(){
        assertEquals(10.0, location.getLatitude());
    }

    @Test
    public void testStreetName(){
        assertEquals("street", location.getStreetName());
    }

    @Test
    public void testGetSpeed(){
        float expected = 22.3694f;
        Location loc = Mockito.mock(Location.class);
        Mockito.when(loc.getSpeed()).thenReturn(10.0f);
        CLocation loc2 = new CLocation(loc);
        LocationData location2 = new LocationData(loc2);
        assertEquals(expected, location2.getSpeed());
    }
}
