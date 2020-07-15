package com.IntegrationTest;

import android.content.Intent;
import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ServiceTestRule;
import com.Logic.Core.LocationService;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeoutException;
import static org.junit.Assert.assertTrue;

public class TestLocationService {

    @Rule
    public final ServiceTestRule serviceRule = new ServiceTestRule();

    @Test
    public void testService() throws TimeoutException{
        serviceRule.startService(new Intent(InstrumentationRegistry.getContext(), LocationService.class));
        assertTrue(LocationService.isServiceRunning());
    }
}