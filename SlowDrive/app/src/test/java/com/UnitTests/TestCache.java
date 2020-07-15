package com.UnitTests;

import com.Logic.Interface.ILocationCache;
import com.Logic.Object.LocationData;
import com.Logic.Cache;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestCache {
	private int Tt;
	private int Tp;

	public TestCache(){

    }

    public void run (){
        testAddLocation();
        testAdd ();
        testNearOffset ();
        return;
    }

    @Test
    public void testAddLocation() {
        final int LOOP_TESTS = 10;

        Cache testCache = new Cache (20);

        // Empty check
        assert  (testCache.empty() == true);




        // Added elements, test
        // Should never be empty as more elements are added
        for (int i = 1; i < LOOP_TESTS; i++)
        {
            testCache.add(new LocationData(i, i, i,true,""));

            assert (testCache.empty() == false);

        }
    }

    @Test
    public void testAdd () {

        final int LOOP_TESTS = 150;
        final int LOOP_TESTS_MODIFIER = 50;
        final int MAX_LONGITUDE = 180;

        ILocationCache testCache = new Cache(20);

        // Null chek


        testCache.add(null);

        assertTrue(testCache.empty());
        assertTrue (testCache.empty());

        assertTrue(testCache.numElements() == 0);
        assert  (testCache.numElements() == 0);

        //duplication test
        testCache.add(new LocationData(1,1,1,true,""));
        testCache.add(new LocationData(1,1,1,true,""));
        assertEquals(testCache.numElements(),1);
        testCache = new Cache(20);

        // Add random data, both faulty and real
        for (int i = -(LOOP_TESTS / 2); i < LOOP_TESTS; i++) {

            int longitude = i * (i - LOOP_TESTS_MODIFIER);
            int latitude = i - LOOP_TESTS_MODIFIER;
            int speedLimit = i;

            // Arguments that are not valid
            boolean valid = longitude < -MAX_LONGITUDE && longitude > MAX_LONGITUDE &&
                    latitude < -MAX_LONGITUDE / 2 && latitude > MAX_LONGITUDE / 2 && speedLimit < 0;



                LocationData data = new LocationData(longitude, latitude, speedLimit,true,"");
                boolean x = testCache.add(data);

                if (valid) {
                    assert(x);
                } else {
                    assert (!x);
                }


        }

    }
    @Test
    public void testNearOffset () {

        final int LOOP_DIST_TESTS = 10;

        Cache testCache = new Cache (20);

        // The LocationData for various areas
        LocationData pembina = new LocationData (-97.138451, 49.895077, 60,true,"");
        LocationData queenston = new LocationData (-98.813873, 53.760860, 50,true,"");

        LocationData res = null;
        LocationData res2 = null;


        // Empty cache test

            // The offset shouldn't have an effect on this
            for (int i = 0; i < LOOP_DIST_TESTS; i++)
            {
                res = testCache.findNearest(pembina, i);
                res2 = testCache.findNearest(queenston, i);
                assert (res==null);
                assert (res2==null);
            }





        // Null test


            assert(testCache.findNearest (null)==null);




        // Test negative offset

        for (int i = -LOOP_DIST_TESTS; i >= 0; i++) {
                assert (testCache.findNearest(pembina, i) == null);



        }

        // Self check
        testCache.add(pembina);
        assertTrue (testCache.findNearest(pembina).equals( pembina));
        for (int i = 0; i < LOOP_DIST_TESTS; i++)
        {
            // Test pembina

            assertTrue (testCache.findNearest(pembina, (int) (Math.random () * 100)).equals(pembina));




            // Testing areas near pembina requires white box testing
        }

        // Adding random items into cache
        testCache =new Cache(20);

        for (int i = 0; i < testCache.getMaxSize(); i++)
        {
            LocationData temp = new LocationData (Math.random () * 50, Math.random () * 10, i,true,"");
           // System.out.println(temp.longitude+" "+temp.latitude);
            // Add faulty data
            testCache.add (temp);
        }

        // Adding real item to full cache
        testCache.add (pembina);
        // I need to know the implementation in order to know the distance measure of the degrees
        // However, we can 100% accuracy approximate by using infinity (or int.max)

        for (int i = 0; i < LOOP_DIST_TESTS; i++) {

            assertTrue(testCache.findNearest(
                    new LocationData(-97.1384 - (i / 100.), 49.895071 - (i / 100.), i, true, ""), Integer.MAX_VALUE).equals(pembina));
        }

        testCache =new Cache(20);

        for (int i = 0; i < testCache.getMaxSize(); i++)
        {
            LocationData temp = new LocationData (Math.random () * 50, Math.random () * 10, i,true,"");
            // System.out.println(temp.longitude+" "+temp.latitude);
            // Add faulty data
            testCache.add (temp);
        }
        for (int i = 0; i < LOOP_DIST_TESTS; i++)
        {

            assertTrue(testCache.findNearest(pembina,0)==null);


        }
        testCache =new Cache(20);
        for (int i = 0; i < testCache.getMaxSize(); i++)
        {
            LocationData temp = new LocationData (i, i, i,true,"");
            // System.out.println(temp.longitude+" "+temp.latitude);
            // Add faulty data
            testCache.add (temp);
            if(i< testCache.getMaxSize()-1){
                testCache.findNearest(new LocationData(i,i,i,true,""),0);
            }
        }

        testCache.add(new LocationData (21, 21, 21,true,"")); //should replace 20
        LocationData k=testCache.findNearest(new LocationData(20,20,20,true,""),0);

        assertEquals(k,null);




    }


}
