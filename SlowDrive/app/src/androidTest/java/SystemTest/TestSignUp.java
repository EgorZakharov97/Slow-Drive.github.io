package SystemTest;


import utils.DriverInfo.FakeDriverInfo;
import utils.TestUtils;
import android.Manifest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import com.Database.Interface.IDriverDB;
import com.R;
import com.UI.SplashScreenActivity;

import com.application.Services;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.junit.Assert.*;
//Note if test not running with intellIJ goto File -> Settings ->Build,Execution, Deployment -> Build Tools -> Gradle
//Change running test tests using to Intellij IDEA
public class TestSignUp {
    @Rule
    public ActivityTestRule<SplashScreenActivity> activityRule
            = new ActivityTestRule<>(SplashScreenActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Test
    public void TestNewUser(){
        //Sign up Test
        onView(withText("SIGN-UP")).perform(click());
        onView(withId(R.id.firstname_su)).perform(typeText(FakeDriverInfo.firstName),closeSoftKeyboard());
        onView(withId(R.id.lastname_su)).perform(typeText(FakeDriverInfo.lastName),closeSoftKeyboard());
        onView(withId(R.id.username_su)).perform(typeText(FakeDriverInfo.userName),closeSoftKeyboard());
        onView(withId(R.id.emailaddress_su)).perform(typeText(FakeDriverInfo.email),closeSoftKeyboard());
        onView(withId(R.id.password_su)).perform(typeText(FakeDriverInfo.password),closeSoftKeyboard());
        onView(withId(R.id.repassword_su)).perform(typeText(FakeDriverInfo.password),closeSoftKeyboard());
        onView(withId(R.id.signup_submit)).perform(click());
        IDriverDB db=  Services.getDriverDB();
        //Did we add this user into Database
        assertNotEquals(db.getDriver("User1"),null);
        onView(withId(R.id.btn_settings)).perform(click());
        //return to front page
        onView(withId(R.id.logout)).perform(scrollTo(),click());

    }
    @After
    public void clean(){
        //remove the test user
        TestUtils.clean();
    }

}
