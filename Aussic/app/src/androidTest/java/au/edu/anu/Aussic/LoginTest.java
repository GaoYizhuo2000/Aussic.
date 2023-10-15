package au.edu.anu.Aussic;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import au.edu.anu.Aussic.controller.loginPages.LoginActivity;

/**
 * Use Espresso to test login part.
 *
 */
public class LoginTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityRule = new ActivityScenarioRule<>(LoginActivity.class);


    @Test
    public void testLoginFunctionality() {
        onView(withId(R.id.UserName)).perform(typeText("comp2100@anu.edu.au"));
        closeSoftKeyboard();
        onView(withId(R.id.UserPassword)).perform(typeText("comp2100"));
        closeSoftKeyboard();
        onView(withId(R.id.SignIN)).perform(click());

        //onView(withId(R.id.home)).check(matches(isDisplayed()));
    }

}
