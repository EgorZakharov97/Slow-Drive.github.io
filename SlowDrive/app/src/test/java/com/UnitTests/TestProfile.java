package com.UnitTests;

import com.Logic.Static.Profile;
import com.Logic.Object.Driver;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;


import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TestProfile {
    @Mock
    private Driver d =mock(Driver.class);
    @Before
    public void setUp(){
        Profile.updateDriver(d);
    }
    @Test
    public void testProfile(){
        when(d.getUserName()).thenReturn("User");
        assertTrue(Profile.getCurrentDriver().getUserName().equals("User"));
    }
}
