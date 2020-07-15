package com.UnitTests;

import android.location.Location;

import com.Logic.Object.CLocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static junit.framework.TestCase.*;

public class TestCLocation {
    // CLocation is the child of Location. When I create Location object manually, for some reason the setLongitude and setLatitude does not perform their job and getLatitude returns 0.0, so there is no way for now to test CLocation.

    private final static double LATITUDE = 10.0;
    private final static double LONGITUDE = 20.0;
    private final static float SPEED_MPS = 30.0f;

    @Mock
    Location loc;
    CLocation location;

    @Before
    public void before(){
        loc = Mockito.mock(Location.class);
        Mockito.when(loc.getLatitude()).thenReturn(LATITUDE);
        Mockito.when(loc.getLongitude()).thenReturn(LONGITUDE);
        Mockito.when(loc.getSpeed()).thenReturn(SPEED_MPS);

        location = new CLocation(loc);
    }

    @Test
    public void testLatitude(){
        assertEquals(LATITUDE, location.getLatitude());
    }

    @Test
    public void testLongitude(){
        assertEquals(LONGITUDE, location.getLongitude());
    }

    @Test
    public void testSpeedInMPS(){
        float expected = 67.1082f;
        assertEquals(expected, location.getSpeed());
    }

    @Test
    public void testDisplaySpeedInKPH(){
        int expected = 108;
        CLocation.setUseMeters(true);
        assertEquals(expected, location.getDisplaySpeed());
    }

    @Test
    public void testDisplaySpeedInMPH(){
        int expected = 67;
        CLocation.setUseMeters(false);
        assertEquals(expected, location.getDisplaySpeed());
    }

    @Test
    public void testHasAllData(){
        assertFalse(location.hasAllData());
    }

    @Test
    public void testWithAllData(){
        location = new CLocation(loc, 40, "Street");
        assertTrue(location.hasAllData());
    }
}
