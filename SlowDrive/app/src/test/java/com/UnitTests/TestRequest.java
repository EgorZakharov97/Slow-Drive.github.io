package com.UnitTests;

import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;

import com.Logic.Object.LocationData;
import com.Logic.Object.CLocation;
import com.Logic.Interface.ISpeedRequestHandler;

import static junit.framework.TestCase.*;

import org.junit.Before;
import org.junit.Test;

public class TestRequest {

    AppHelper helper;

    @Before
    public void init(){
        helper = new AppHelper();
    }

    @Test
    public void testRequest(){
        Location loc = new Location("");
        loc.setLongitude(-10);
        loc.setLatitude(10);

        CLocation location = new CLocation(loc);

    }

    private static void onLocationReceived(LocationData location){
        System.out.println("LOCATION_RECEIVED: " + location.getStreetName());
        assertNotNull(location);
    }

    private class AppHelper extends AppCompatActivity implements ISpeedRequestHandler {
        public AppHelper(){

        }

        @Override
        public void OnRequestFinished(LocationData location) {
            TestRequest.onLocationReceived(location);
        }
    }
}