package com.shehabsalah.bakingapp.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.ui.fragments.RecipeFragment;
import com.shehabsalah.bakingapp.util.Config;
import com.shehabsalah.bakingapp.util.SimpleIdlingResource;

public class MainActivity extends AppCompatActivity {

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            // Create new RecipeFragment and added to recipe_container
            Fragment fragment = new RecipeFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_container, fragment, Config.MAIN_FRAGMENT_TAG)
                    .commit();
        }else{
            // Restore the Fragment
            if (getFragmentManager().findFragmentByTag(Config.MAIN_FRAGMENT_TAG) != null)
                getFragmentManager().findFragmentByTag(Config.MAIN_FRAGMENT_TAG).getRetainInstance();
        }
        // Get the IdlingResource instance
        getIdlingResource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Saved the Fragment
        if (getFragmentManager().findFragmentByTag(Config.MAIN_FRAGMENT_TAG) != null)
            getFragmentManager().findFragmentByTag(Config.MAIN_FRAGMENT_TAG).setRetainInstance(true);
    }
}
