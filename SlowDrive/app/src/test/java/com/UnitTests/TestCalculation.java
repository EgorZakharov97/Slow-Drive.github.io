package com.UnitTests;

import com.Logic.Object.LocationData;
import com.Logic.Static.Calculation;

import static junit.framework.TestCase.*;

import org.junit.Test;

public class TestCalculation {

    @Test
    public void testCalcDistance(){
        int expected = 1499099;

        LocationData loc1 = new LocationData(10, 20, 40, true, "w");
        LocationData loc2 = new LocationData(20, 30, 40, true, "w");

        int distance = Calculation.calcDistance(loc1, loc2);

        assertEquals(expected, distance);
    }

    @Test
    public void testDegToRad(){
        double expected = 4.886921905584122;
        double degree = 280d;

        double response = Calculation.degToRad(degree);

        assertEquals(expected, response);
    }
}
