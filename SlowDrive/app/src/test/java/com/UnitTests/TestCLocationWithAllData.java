package com.UnitTests;

import android.location.Location;

import com.Logic.Object.CLocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCLocationWithAllData {

    private final static double LATITUDE = 10.0;
    private final static double LONGITUDE = 20.0;
    private final static float SPEED_MPS = 30.0f;
    private final static int SPEED_LIMIT = 40;
    private final static String STREET_NAME = "STREET";

    @Mock
    Location loc;
    CLocation location;

    @Before
    public void before(){
        loc = Mockito.mock(Location.class);
        Mockito.when(loc.getLatitude()).thenReturn(LATITUDE);
        Mockito.when(loc.getLongitude()).thenReturn(LONGITUDE);
        Mockito.when(loc.getSpeed()).thenReturn(SPEED_MPS);

        location = new CLocation(loc, SPEED_LIMIT, STREET_NAME);
    }

    @Test
    public void testHasAllData(){
        assertTrue(location.hasAllData());
    }

    @Test
    public void testStreetName(){
        assertEquals(STREET_NAME, location.getStreetName());
    }

    @Test
    public void test_speed_limit_in_kph(){
        int expected = 65;
        CLocation.setUseMeters(true);
        assertEquals(expected, location.getSpeedLimit());
    }

    @Test
    public void test_speed_limit_in_mph(){
        int expected = 40;
        CLocation.setUseMeters(false);
        assertEquals(expected, location.getSpeedLimit());
    }
}
