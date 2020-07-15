package com.UnitTests;

import com.Logic.Object.CLocation;
import com.Logic.Static.State;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestState {
    @Mock
    private CLocation location= mock(CLocation.class);

    @Test
    public void testNotExceeding(){
        //test not over speeding
        when(location.hasAllData()).thenReturn(true);
        when(location.getDisplaySpeed()).thenReturn(50);
        when(location.getSpeedLimit()).thenReturn(50);
        //within offset, should accept
        State.checkIfExceeding(location);
        assertFalse(State.isExceeding());
    }
    @Test
    public void testExceeding(){
        //test not over speeding
        when(location.hasAllData()).thenReturn(true);
        when(location.getDisplaySpeed()).thenReturn(50);
        when(location.getSpeedLimit()).thenReturn(40);
        //within offset, should accept
        State.checkIfExceeding(location);
        assert(State.isExceeding());
    }

}
