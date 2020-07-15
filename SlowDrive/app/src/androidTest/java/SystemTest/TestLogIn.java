package SystemTest;

import utils.DriverInfo.FakeDriverInfo;
import utils.TestUtils;
import android.Manifest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.R;
import com.UI.SplashScreenActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

//Note if test not running with intellIJ goto File -> Settings ->Build,Execution, Deployment -> Build Tools -> Gradle
//Change running test tests using to Intellij IDEA
public class TestLogIn {
    @Rule
    public ActivityTestRule<SplashScreenActivity> activityRule
            = new ActivityTestRule<>(SplashScreenActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Before
    public void setUp(){
        TestUtils.setUp();
    }
    @Test
    public void TestLogin(){
        //Login Test
        onView(withText("LOG-IN")).perform(click());
        onView(withId(R.id.username_li)).perform(typeText(FakeDriverInfo.userName),closeSoftKeyboard());
        onView(withId(R.id.password_li)).perform(typeText(FakeDriverInfo.password),closeSoftKeyboard());
        onView(withId(R.id.login_submit)).perform(click());
        //we should be in main page with Profile
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
