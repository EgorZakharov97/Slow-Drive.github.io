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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.not;

public class TestProfile {
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
    public void TestGuestProfile(){
        //go to profile
        onView(withId(R.id.temp_button_to_main)).perform(click());
        onView(withId(R.id.btn_settings)).perform(click());
        //checks
        onView(withId(R.id.Profile_hint)).check(matches(withText("Guest's Profile")));
        onView(withId(R.id.emailPrompt)).check(matches(withText("")));
        onView(withId(R.id.currPassPrompt)).check(matches(not(isEnabled())));
        onView(withId(R.id.newPassPrompt)).check(matches(not(isEnabled())));
        onView(withId(R.id.confirmPassPrompt)).check(matches(not(isEnabled())));
        onView(withId(R.id.password_submit)).check(matches(not(isEnabled())));
    }
    @Test
    public void TestLogInProfile(){
        //go to profile
        onView(withText("LOG-IN")).perform(click());
        onView(withId(R.id.username_li)).perform(typeText(FakeDriverInfo.userName),closeSoftKeyboard());
        onView(withId(R.id.password_li)).perform(typeText(FakeDriverInfo.password),closeSoftKeyboard());
        onView(withId(R.id.login_submit)).perform(click());
        onView(withId(R.id.btn_settings)).perform(click());
        //checks
        onView(withId(R.id.Profile_hint)).check(matches(withText( FakeDriverInfo.firstName+"'s Profile")));
        onView(withId(R.id.emailPrompt)).check(matches(withText(FakeDriverInfo.email)));
        onView(withId(R.id.currPassPrompt)).check(matches(isEnabled()));
        onView(withId(R.id.newPassPrompt)).check(matches(isEnabled()));
        onView(withId(R.id.confirmPassPrompt)).check(matches(isEnabled()));
    }
    @After
    public void clean(){
        //remove the test user
        TestUtils.clean();
    }
}
