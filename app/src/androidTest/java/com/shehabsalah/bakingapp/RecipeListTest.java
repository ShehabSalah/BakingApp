package com.shehabsalah.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.shehabsalah.bakingapp.ui.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

/**
 * Created by Shehab Salah on 6/13/17.
 * Testing Backing App navigation
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListTest {
    @Rule public ActivityTestRule<MainActivity> mainActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource(){
        mIdlingResource = mainActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void onRecipeListItemClicked_openRecipeStepsList(){
        onData(anything()).inAdapterView(withId(R.id.recipe_list)).atPosition(0).perform(click());
        onView(allOf(withId(R.id.recipe_details_container),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .check(matches(isCompletelyDisplayed()));
        // Testing the list of recipe steps
        onStepListItemClicked_openStepDetails();
        // Testing Next Button on Step Details
        onNextButtonClicked_goToNextStep();
        // Testing Next Button on Step Details
        onPreviousButtonClicked_goToPreviousStep();

    }

    private void onStepListItemClicked_openStepDetails(){
        onView(withId(R.id.recipe_details_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView((withId(R.id.shortDescribe))).check(matches(withText("Recipe Introduction")));
    }

    private void onNextButtonClicked_goToNextStep(){
        onView(withId(R.id.next_button)).perform(click());
        onView((withId(R.id.shortDescribe))).check(matches(withText("Starting prep")));
    }

    private void onPreviousButtonClicked_goToPreviousStep(){
        onView(withId(R.id.previous_button)).perform(click());
        onView((withId(R.id.shortDescribe))).check(matches(withText("Recipe Introduction")));
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
