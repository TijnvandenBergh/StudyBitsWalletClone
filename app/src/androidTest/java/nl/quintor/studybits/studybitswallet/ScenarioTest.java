package nl.quintor.studybits.studybitswallet;

import android.Manifest;
import android.content.Intent;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.*;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import android.util.Log;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;


import nl.quintor.studybits.studybitswallet.university.UniversityActivity;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@LargeTest
public class ScenarioTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<UniversityActivity> universityActivityRule = new ActivityTestRule<>(UniversityActivity.class, true, false);

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

    @Before
    public void setTimeouts() {
        Log.d("STUDYBITS", "Setting timeouts to 3 minutes");

        IdlingPolicies.setMasterPolicyTimeout(180, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(180, TimeUnit.SECONDS);
    }

    @Test
    public void fullScenarioTest() {
        Log.d("STUDYBITS", "Starting test");
        // Reset
        onView(withId(R.id.fab))
                .perform(click());
        Log.d("STUDYBITS", "Clicked reset");

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText("Successfully reset")))
                .check(matches(isDisplayed()));

        Log.d("STUDYBITS", "Successfully reset");




        // Launch connection dialog
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(TestConfiguration.CONNECTION_URI_RUG);

        universityActivityRule.launchActivity(intent);

        // Enter studentID
        onView(withId(R.id.student_id_text))
                .check(matches(isDisplayed()))
                .perform(typeText("2102241"));

        // Enter password
        onView(withId(R.id.password_text))
                .check(matches(isDisplayed()))
                .perform(typeText("test1234"));

        // Click connect
        onView(withText(R.string.connect))
                .perform(click());

        // Check connection to university
        onView(withText("Rijksuniversiteit Groningen"))
                .check(matches(isDisplayed()));

        Log.e("STUDYBITS", "REACHED");
        // Navigate to credentials via pressing back on toolbar
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withId(R.id.button_credential))
                .perform(click());

//        //Check if credentials are present
//        Log.e("MARCO", "Komt hier");
//        onView(allOf(, withText("Rijksuniversiteit Groningen")));
//        Log.e("MARCO", "En hier");

       // Accept credential
        onView(withId(R.id.credential_fragment)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Launch connection dialog
        intent = new Intent();

        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(TestConfiguration.CONNECTION_URI_GENT);

        universityActivityRule.launchActivity(intent);

        // Enter text
        onView(withId(R.id.student_id_text))
                .check(matches(isDisplayed()));

        // Click connect
        onView(withText(R.string.connect))
                .perform(click());

        // Check connection to university
        onView(withText("Universiteit Gent"))
                .check(matches(isDisplayed()));


        // Navigate to exchange positions via pressing back on toolbar
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withId(R.id.button_exchange_position))
                .perform(click());

        // Accept exchange position
        onView(withText("MSc Marketing"))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText("Send"))
                .check(matches(isDisplayed()))
                .perform(click());

        // Check result
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText("You're going abroad!")))
                .check(matches(isDisplayed()));
    }
}
