package com.shehabsalah.bakingapp.ui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.data.recipes.RecipeHolder;
import com.shehabsalah.bakingapp.ui.fragments.StepFragment;
import com.shehabsalah.bakingapp.util.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsActivity extends AppCompatActivity {
    @BindView(R.id.previous_button)
    TextView previous;
    @BindView(R.id.next_button)
    TextView next;
    RecipeHolder recipeHolder;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            // Get the extra recipe
            Intent intent = getIntent();
            if (intent.hasExtra(Config.STEPS_INTENT)){
                recipeHolder = intent.getParcelableExtra(Config.STEPS_INTENT);
                currentPosition = intent.getIntExtra(Config.STEP_INDEX,0);
            }

            setContentView(R.layout.activity_step_details);
            // If the coming data not the ingredients of the recipe, get the video url
            String videoURL = currentPosition==0?null:recipeHolder.getSteps().get(currentPosition-1).getVideoURL();
            // Initialize video full screen
            fullScreenMode(videoURL);
            // Initialize next button and previous button
            ButterKnife.bind(this);

            if (recipeHolder!=null){
                //display step data
                bindViews(recipeHolder, currentPosition);
            }else finish();

        }else{
            // Restore recipe data
            recipeHolder = savedInstanceState.containsKey(Config.RECIPE_STEPS_STATE)?
                    (RecipeHolder) savedInstanceState.getParcelable(Config.RECIPE_STEPS_STATE):null;
            // Restore step position
            currentPosition = savedInstanceState.containsKey(Config.STEP_POSITION_STATE)?
                    savedInstanceState.getInt(Config.STEP_POSITION_STATE):-1;
            // Restore step fragment
            if (getFragmentManager().findFragmentByTag(Config.INNER_DETAILS_TAG) != null)
                getFragmentManager().findFragmentByTag(Config.INNER_DETAILS_TAG).getRetainInstance();
            // Get the video url
            String videoURL = currentPosition==0?null:recipeHolder.getSteps().get(currentPosition-1).getVideoURL();
            setContentView(R.layout.activity_step_details);
            // Initialize video full screen
            fullScreenMode(videoURL);
            // Initialize next button and previous button
            ButterKnife.bind(this);
            // Handle the next and previous navigation
            nav_controller(currentPosition, recipeHolder.getSteps().size());
        }

        // On next button click move to next step
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipeHolder!=null){
                    moveTo(nextOf(currentPosition,recipeHolder.getSteps().size()));
                    nav_controller(currentPosition,recipeHolder.getSteps().size());
                }
            }
        });
        //On previous button click move to previous step
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipeHolder!=null){
                    moveTo(previousOf(currentPosition));
                    nav_controller(currentPosition,recipeHolder.getSteps().size());
                }
            }
        });

        // Display recipe name in actionBar title
        if (recipeHolder!=null){
            if (getSupportActionBar()!=null)
                getSupportActionBar().setTitle(recipeHolder.getName());
        }
    }
    /**
     * Add StepFragment to inner_step_container and display the step data at given position
     * @param recipeHolder data to be displayed
     * @param position selected step position to display
     * */
    private void bindViews(final RecipeHolder recipeHolder, int position){
        // Handle the next and previous navigation
        nav_controller(position, recipeHolder.getSteps().size());
        // Create new StepFragment and added to inner_step_container
        StepFragment fragment = new StepFragment();
        if (position == 0)
            fragment.setIngredients(recipeHolder.getIngredients());
        else fragment.setStep(recipeHolder.getSteps().get(position - 1));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.inner_step_container, fragment, Config.INNER_DETAILS_TAG)
                .commit();
    }
    /**
     * Handle the next and previous button visibility according to the current position and maximum size
     * @param position current list position
     * @param listSize maximum size
     * */
    private void nav_controller(int position, int listSize){
        if (position == 0) {
            previous.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
        }
        else if(position == listSize){
            next.setVisibility(View.GONE);
            previous.setVisibility(View.VISIBLE);
        }else{
            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
        }
    }
    /**
     * Replace the current step fragment with step fragment of given position
     * @param position position of the new step fragment to replace the current fragment
     * */
    private void moveTo(int position){
        if (recipeHolder!=null){
            StepFragment fragment = new StepFragment();
            if (position == 0)
                fragment.setIngredients(recipeHolder.getIngredients());
            else {
                String videoURL = recipeHolder.getSteps().get(position-1).getVideoURL();
                fullScreenMode(videoURL);
                fragment.setStep(recipeHolder.getSteps().get(position - 1));
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.inner_step_container, fragment, Config.INNER_DETAILS_TAG)
                    .commit();
        }
    }
    /**
     * Handle the fullscreen mode and visibility of the action bar based on device orientation and
     * the current step type.
     * @param videoURL check if the current step has video or not
     * */
    private void fullScreenMode(String videoURL){
        /*
        * If the current step contain video and the device orientation is LANDSCAPE? go to fullscreen
        * mode, otherwise cancel the fullscreen mode
        * */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && currentPosition != 0
                && videoURL != null && !videoURL.isEmpty()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        /*
        * If the current step contain video and the device orientation is LANDSCAPE? Hide the ActionBar
        * mode, otherwise Show the ActionBar
        * */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && currentPosition != 0
                && videoURL != null && !videoURL.isEmpty()) {
            if (getSupportActionBar()!=null && getSupportActionBar().isShowing())
                getSupportActionBar().hide();
        }else{
            if (getSupportActionBar()!=null)
                getSupportActionBar().show();
        }
    }
    /**
     * Handle the increment of positions
     * @param position current step position
     * @param listSize the maximum incremental size or the incremental limit
     * @return the next position of the current position
     * */
    private int nextOf(int position, int listSize){
        if (position < listSize)
            position++;
        currentPosition = position;
        return position;
    }
    /**
     * Handle the decrement of positions
     * @param position current step position
     * @return previous position of the current position
     * */
    private int previousOf(int position){
        if (position > 0)
            position--;
        currentPosition = position;
        return position;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the fragment that contain step details
        if (getFragmentManager().findFragmentByTag(Config.INNER_DETAILS_TAG) != null)
            getFragmentManager().findFragmentByTag(Config.INNER_DETAILS_TAG).setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the current position & current recipe
        outState.putInt(Config.STEP_POSITION_STATE,currentPosition);
        outState.putParcelable(Config.RECIPE_STEPS_STATE,recipeHolder);
        super.onSaveInstanceState(outState);
    }
}