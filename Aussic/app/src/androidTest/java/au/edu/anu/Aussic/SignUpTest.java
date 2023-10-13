package au.edu.anu.Aussic;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import au.edu.anu.Aussic.controller.LoginActivity;
public class SignUpTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityRule = new ActivityScenarioRule<>(LoginActivity.class);
    @Test
    public void testSignUpFunctionality() {

        onView(withId(R.id.SignUP)).perform(click());
        onView(withId(R.id.signUpUsername)).perform(typeText("comp4690@anu.edu.au"));
        onView(withId(R.id.signUpPassword)).perform(typeText("comp4690"));
        onView(withId(R.id.comfirmPassword)).perform((typeText("comp4690")));
        closeSoftKeyboard();
        onView(withId(R.id.SignUP)).perform(click()); //TODO: the id of Sign Up button

        //onView(withId(R.id.home)).check(matches(isDisplayed()));
    }

}
