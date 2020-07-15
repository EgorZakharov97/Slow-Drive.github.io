package com;


import com.UnitTests.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
        @Suite.SuiteClasses({
                TestAudio.class,
                TestCache.class,
                TestCalculation.class,
                TestCLocation.class,
                TestDriver.class,
                TestHistory.class,
                TestHTTPHandler.class,
                TestLocationData.class,
                TestRequest.class,
                TestProfile.class,
                TestPasswordUtils.class,
                TestLogin.class,
                TestSignUp.class,
                TestHistoryMNG_Unit.class,
                TestState.class
})
public class TestAllUnit {

}
