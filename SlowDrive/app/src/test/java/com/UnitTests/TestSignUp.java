package com.UnitTests;

import com.Database.Interface.IDriverDB;
import com.Logic.Manager.SignupManager;
import com.Logic.Object.Driver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestSignUp {
    SignupManager signupManager;
    @Mock
    private IDriverDB d = mock(IDriverDB.class);

    @Before
    public void setUp() {
        signupManager = new SignupManager(d);
    }

    @Test
    public void testAlreadyExist() {

        Driver notnull = new Driver();
        when(d.getDriver("test")).thenReturn(notnull);
        assertFalse(signupManager.startSignup("test", "!12345678Qq", "123", "123", "123@email.com", "!12345678Qq"));

    }

    @Test
    public void testInvalidUserName() {
        when(d.getDriver("test")).thenReturn(null);
        //empty
        assertFalse(signupManager.startSignup("", "123", "123", "123", "123@email.com", "123"));
        //contain " "
        assertFalse(signupManager.startSignup("Te st", "123", "123", "123", "123@email.com", "123"));
    }

    @Test
    public void testInValidPassword() {
        when(d.getDriver("test")).thenReturn(null);
        //empty
        assertFalse(signupManager.startSignup("Test", "", "123", "123", "123@email.com", ""));
        //invalid format
        assertFalse(signupManager.startSignup("Test", "12345678", "123", "123", "123@email.com", "12345678"));
        //re-password fail
        assertFalse(signupManager.startSignup("Test", "!12345678Qq", "123", "123", "123@email.com", "!456Qq"));
    }

    @Test
    public void testInvalidEmail() {
        when(d.getDriver("test")).thenReturn(null);
        //empty
        assertFalse(signupManager.startSignup("Test", "!12345678Qq", "123", "123", "", "!12345678Qq"));
        //invalid
        assertFalse(signupManager.startSignup("Test", "!12345678Qq", "123", "123", "I dont care@", "!12345678Qq"));

    }

    @Test
    public void Valid() {
        when(d.getDriver("test")).thenReturn(null);
        assertTrue(signupManager.startSignup("Test", "!12345678Qq", "123", "123", "123@email.com", "!12345678Qq"));
    }
}


