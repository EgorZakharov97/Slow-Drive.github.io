package IntegrationTest;

        import android.Manifest;
        import androidx.test.rule.ActivityTestRule;
        import androidx.test.rule.GrantPermissionRule;
        import com.Logic.Static.State;
        import com.R;
        import com.UI.SplashScreenActivity;
        import org.junit.Before;
        import org.junit.Rule;
        import org.junit.Test;

        import static androidx.test.espresso.Espresso.onView;
        import static androidx.test.espresso.action.ViewActions.click;
        import static androidx.test.espresso.assertion.ViewAssertions.matches;
        import static androidx.test.espresso.matcher.ViewMatchers.*;
        import static org.hamcrest.core.StringContains.containsString;
public class TestSpeed {
    @Rule
    public ActivityTestRule<SplashScreenActivity> activityRule
            = new ActivityTestRule<>(SplashScreenActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Before
    public void setUp(){
        onView(withId(R.id.temp_button_to_main)).perform(click());
    }
    @Test
    public void showSpeed(){
        onView(withId(R.id.btn_start)).perform(click());
        //show UI?
        onView(withId(R.id.tv_speed)).check(matches(isDisplayed()));
        if(State.metric)
            onView(withId(R.id.tv_speed)).check(matches(withText(containsString("km/h"))));
        else
            onView(withId(R.id.tv_speed)).check(matches(withText(containsString("mile/h"))));
        onView(withId(R.id.btn_start)).perform(click());
    }
    //a different test with the one in settings
    @Test
    public void showSpeedLimit(){
        if(State.showSpeedLimit){
            onView(withId(R.id.img_speed_limit)).check(matches(isDisplayed()));
            onView(withId(R.id.btn_start)).perform(click());
            if(State.metric){
                onView(withId(R.id.speed_limit_unit)).check(matches(withText(containsString("KPH"))));
            }else{
                onView(withId(R.id.speed_limit_unit)).check(matches(withText(containsString("MPH"))));
            }
            onView(withId(R.id.btn_start)).perform(click());
        }
        else{
            onView(withId(R.id.btn_settings)).perform(click());
            //turn on speedLimit
            onView(withId(R.id.sw_speed_limit)).perform(click());
            onView(withId(R.id.back_settings)).perform(click());
            onView(withId(R.id.img_speed_limit)).check(matches(isDisplayed()));
            if(State.metric){
                onView(withId(R.id.speed_limit_unit)).check(matches(withText(containsString("KPH"))));
            }else{
                onView(withId(R.id.speed_limit_unit)).check(matches(withText(containsString("MPH"))));
            }
            //reset
            onView(withId(R.id.btn_start)).perform(click());
            onView(withId(R.id.btn_settings)).perform(click());
            //turn off speedLimit
            onView(withId(R.id.sw_speed_limit)).perform(click());
        }
    }
    @Test
    public void warn(){
        onView(withId(R.id.btn_start)).perform(click());
        if(State.warn){
            onView(withId(R.id.warn)).check(matches(withText("Be careful. You've oversped here in the past.")));
        }
        else{
            onView(withId(R.id.warn)).check(matches(withText("")));
        }
        onView(withId(R.id.btn_start)).perform(click());
    }

}
