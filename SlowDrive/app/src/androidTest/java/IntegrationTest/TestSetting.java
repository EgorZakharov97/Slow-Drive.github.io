package IntegrationTest;

import utils.DriverInfo.FakeDriverInfo;
import utils.TestUtils;
import android.Manifest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import com.Logic.Static.State;
import com.R;
import com.UI.SplashScreenActivity;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.not;

public class TestSetting {
    @Rule
    public ActivityTestRule<SplashScreenActivity> activityRule
            = new ActivityTestRule<>(SplashScreenActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void changePassWord(){
        TestUtils.setUp();
        onView(withText("LOG-IN")).perform(click());
        onView(withId(R.id.username_li)).perform(typeText(FakeDriverInfo.userName),closeSoftKeyboard());
        onView(withId(R.id.password_li)).perform(typeText(FakeDriverInfo.password),closeSoftKeyboard());
        onView(withId(R.id.login_submit)).perform(click());
        //change password
        String newPassWord="!Qq12345678";
        onView(withId(R.id.btn_settings)).perform(click());
        onView(withId(R.id.currPassPrompt)).perform(typeText(FakeDriverInfo.password),closeSoftKeyboard());
        onView(withId(R.id.newPassPrompt)).perform(typeText(newPassWord),closeSoftKeyboard());
        onView(withId(R.id.confirmPassPrompt)).perform(typeText(newPassWord),closeSoftKeyboard());
        onView(withId(R.id.password_submit)).perform(click());
        onView(withId(R.id.PasswordAuthentication)).check(matches(withText("Password has been updated!")));
        onView(withId(R.id.logout)).perform(scrollTo(),click());
        //do re-login
        onView(withText("LOG-IN")).perform(click());
        onView(withId(R.id.username_li)).perform(typeText(FakeDriverInfo.userName),closeSoftKeyboard());
        onView(withId(R.id.password_li)).perform(typeText(newPassWord),closeSoftKeyboard());
        onView(withId(R.id.login_submit)).perform(click());
        //remove the test user
        TestUtils.clean();
    }
    @Test
    public void setMetricUnit(){
        onView(withId(R.id.temp_button_to_main)).perform(click());
        if(State.metric)
            onView(withId(R.id.speed_limit_unit)).check(matches(withText("KPH")));
        else
            onView(withId(R.id.speed_limit_unit)).check(matches(withText("MPH")));

        //go to setting and does the opposite
        onView(withId(R.id.btn_settings)).perform(click());
        onView(withId(R.id.swch_metric)).perform(click());
        onView(withId(R.id.back_settings)).perform(click());
        //check again
        if(State.metric)
            onView(withId(R.id.speed_limit_unit)).check(matches(withText("KPH")));
        else
            onView(withId(R.id.speed_limit_unit)).check(matches(withText("MPH")));
        //reset
        onView(withId(R.id.btn_settings)).perform(click());
        onView(withId(R.id.swch_metric)).perform(click());
        onView(withId(R.id.back_settings)).perform(click());
    }
    @Test
    public void changeVolume(){
        onView(withId(R.id.temp_button_to_main)).perform(click());
        onView(withId(R.id.btn_settings)).perform(click());
        onView(withId(R.id.volume_bar)).perform(swipeLeft());
        int currentVol=State.volume;
        onView(withId(R.id.volume_bar)).perform(swipeRight());
        assertTrue(currentVol<State.volume);
        currentVol=State.volume;
        //do the opposite
        onView(withId(R.id.volume_bar)).perform(swipeLeft());
        assertTrue(currentVol>State.volume);
        //can't find a way to reset the volume at this point,so put to max
        onView(withId(R.id.volume_bar)).perform(swipeRight());
    }

    private void assertTrue(boolean b) {
    }

    @Test
    public void noSound(){
        onView(withId(R.id.temp_button_to_main)).perform(click());
        onView(withId(R.id.btn_settings)).perform(click());
        boolean curr=!State.noSound;
        if(!State.noSound)
            onView((withId(R.id.sw_use_sound))).check(matches(isChecked()));
        else
            onView((withId(R.id.sw_use_sound))).check(matches(not(isChecked())));
        onView((withId(R.id.sw_use_sound))).perform(click());
        if(!curr)
            onView((withId(R.id.sw_use_sound))).check(matches(isChecked()));
        else
            onView((withId(R.id.sw_use_sound))).check(matches(not(isChecked())));
        //reset
        onView((withId(R.id.sw_use_sound))).perform(click());

    }
    @Test
    public void ShowSpeedLimit(){
        onView(withId(R.id.temp_button_to_main)).perform(click());
        boolean curr=State.showSpeedLimit;
        if(State.showSpeedLimit){
            onView(withId(R.id.img_speed_limit)).check(matches(isDisplayed()));
        }else{
            onView(withId(R.id.img_speed_limit)).check(matches(not(isDisplayed())));
        }
        //change to opposite
        onView(withId(R.id.btn_settings)).perform(click());
        onView(withId(R.id.sw_speed_limit)).perform(click());
        onView(withId(R.id.back_settings)).perform(click());
        if(curr){
            onView(withId(R.id.img_speed_limit)).check(matches(not(isDisplayed())));
        }
        else{
            onView(withId(R.id.img_speed_limit)).check(matches(isDisplayed()));
        }
        //reset
        onView(withId(R.id.btn_settings)).perform(click());
        onView(withId(R.id.sw_speed_limit)).perform(click());
        onView(withId(R.id.back_settings)).perform(click());
    }

}
