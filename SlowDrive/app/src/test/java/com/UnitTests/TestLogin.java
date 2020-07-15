package com.UnitTests;

import com.Database.Interface.IDriverDB;
import com.Logic.Manager.LoginManager;
import com.Logic.Utils.PasswordUtils;
import com.Logic.Object.Driver;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestLogin {
    @Mock
    private IDriverDB d =mock(IDriverDB.class);

    @Test
    public void testLogin(){
        //invalid login
        LoginManager mng= new LoginManager(d);
        //no account
        when(d.getDriver("test")).thenReturn(null);
        assertFalse(mng.startLogin("test","123"));

        //wrong password
        when(d.getDriver("test")).thenReturn(new Driver("","","test","123",""));
        when(d.getDriverSalt("test")).thenReturn("123");
        assertFalse(mng.startLogin("test","456"));

        //valid
        when(d.getDriverSalt("test")).thenReturn("123");
        String secured= PasswordUtils.generateSecurePassword("123","123");
        when(d.getDriver("test")).thenReturn(new Driver("","","test",secured,""));
        assertTrue(mng.startLogin("test","123"));

    }
}
