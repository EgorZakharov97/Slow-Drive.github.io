package com;

import com.IntegrationTest.TestHistoryMNG;
import com.IntegrationTest.TestLoginMNG;
import com.IntegrationTest.TestSignupMNG;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestHistoryMNG.class,
        TestLoginMNG.class,
        TestSignupMNG.class

})
public class TestAllInter {
}
