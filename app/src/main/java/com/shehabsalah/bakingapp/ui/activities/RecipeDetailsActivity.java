package com.shehabsalah.bakingapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.data.RecipeHolder;
import com.shehabsalah.bakingapp.ui.fragments.RecipeDetailsFragment;
import com.shehabsalah.bakingapp.ui.fragments.StepFragment;
import com.shehabsalah.bakingapp.util.Config;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.OnRecipeDetailsClickListener{
    RecipeHolder recipeHolder;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        // If it two pane mode
        if (findViewById(R.id.activity_step_details) !=null){
            // Make the two pane to true
            Config.TwoPane = true;
            if (savedInstanceState==null){
                // Get the extra recipe
                Intent intent = getIntent();
                // If the recipe come from the MainActivity otherwise it comes from widget
                if (intent.hasExtra(Config.RECIPE_ITEM))
                    recipeHolder = intent.getParcelableExtra(Config.RECIPE_ITEM);
                 else recipeHolder = intent.getParcelableExtra(Config.WIDGET_GET_IN);
                // If the intent has Extra and retrieved successfully
                if (recipeHolder!=null){
                    // Create new RecipeDetailsFragment and added to recipe_details_container
                    RecipeDetailsFragment fragment = new RecipeDetailsFragment();
                    fragment.addRecipe(recipeHolder);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.recipe_details_container, fragment, Config.DETAILS_FRAGMENT_TAG)
                            .commit();
                    /*
                      We in Two Pane mode so, we need to fill the step details with the data.
                      In This case, the activity is started for it's first time, so the step details
                      will fill with the first item in the step list which is 0
                      */
                    bindViews(recipeHolder, 0);
                }
            }else{
                // Restore the recipe steps list fragment
                if (getFragmentManager().findFragmentByTag(Config.DETAILS_FRAGMENT_TAG) != null)
                    getFragmentManager().findFragmentByTag(Config.DETAILS_FRAGMENT_TAG).getRetainInstance();
                // Restore recipes data
                recipeHolder = savedInstanceState.containsKey(Config.RECIPE_STEPS_STATE)?
                        (RecipeHolder) savedInstanceState.getParcelable(Config.RECIPE_STEPS_STATE):null;
                // Restore the step position
                currentPosition = savedInstanceState.containsKey(Config.STEP_POSITION_STATE)?
                        savedInstanceState.getInt(Config.STEP_POSITION_STATE):-1;
                // Restore the fragment that contain step details
                if (getFragmentManager().findFragmentByTag(Config.INNER_DETAILS_TAG) != null)
                    getFragmentManager().findFragmentByTag(Config.INNER_DETAILS_TAG).getRetainInstance();


            }

        }else{
            // If it not in two pane mode, make the two pane to false
            Config.TwoPane = false;
            if (savedInstanceState==null){
                // Get the extra recipe
                Intent intent = getIntent();
                // If the recipe come from the MainActivity otherwise it comes from widget
                if (intent.hasExtra(Config.RECIPE_ITEM))
                    recipeHolder = intent.getParcelableExtra(Config.RECIPE_ITEM);
                else recipeHolder = intent.getParcelableExtra(Config.WIDGET_GET_IN);
                // Create new RecipeDetailsFragment and added to recipe_details_container
                RecipeDetailsFragment fragment = new RecipeDetailsFragment();
                fragment.addRecipe(recipeHolder);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_details_container, fragment, Config.DETAILS_FRAGMENT_TAG)
                        .commit();
                // If the intent came from the widget, open the Recipe Ingredients
                if (intent.hasExtra(Config.WIDGET_GET_IN)){
                    intent = new Intent(this, StepDetailsActivity.class);
                    intent.putExtra(Config.STEPS_INTENT,recipeHolder);
                    intent.putExtra(Config.STEP_INDEX, 0);
                    startActivity(intent);
                }

            }else{
                // Restore the recipe steps list fragment
                if (getFragmentManager().findFragmentByTag(Config.DETAILS_FRAGMENT_TAG) != null)
                    getFragmentManager().findFragmentByTag(Config.DETAILS_FRAGMENT_TAG).getRetainInstance();
            }
        }
        // Display recipe name in actionBar title
        if (recipeHolder!=null)
            if (getSupportActionBar()!=null)
                getSupportActionBar().setTitle(recipeHolder.getName());
    }
    /**
     * Add the step data to step detail container fragment at given position, used in Two Pane only.
     * @param recipeHolder holder of the Ingredients & Steps
     * @param position Position of the step that need to fill
     * */
    private void bindViews(final RecipeHolder recipeHolder, int position){
        StepFragment fragment = new StepFragment();
        // If the position is 0 display the recipe Ingredients otherwise display the step
        if (position == 0)
            fragment.setIngredients(recipeHolder.getIngredients());
        else fragment.setStep(recipeHolder.getSteps().get(position - 1));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.inner_step_container, fragment, Config.INNER_DETAILS_TAG)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Saved the recipe steps list fragment
        if (getFragmentManager().findFragmentByTag(Config.DETAILS_FRAGMENT_TAG) != null)
            getFragmentManager().findFragmentByTag(Config.DETAILS_FRAGMENT_TAG).setRetainInstance(true);

        // If in two pane mode? save the fragment that contain step details
        if (Config.TwoPane){
            if (getFragmentManager().findFragmentByTag(Config.INNER_DETAILS_TAG) != null)
                getFragmentManager().findFragmentByTag(Config.INNER_DETAILS_TAG).setRetainInstance(true);
        }
    }

    @Override
    public void onRecipeStepSelected(RecipeHolder recipeHolder, int position) {
        if (recipeHolder!=null){
            /*
                If it two pane, replace the step details fragment with the selected one, otherwise
                go to StepDetailsActivity to display the step details.
             */
            if (Config.TwoPane){
                StepFragment fragment = new StepFragment();
                if (position == 0) fragment.setIngredients(recipeHolder.getIngredients());
                else fragment.setStep(recipeHolder.getSteps().get(position - 1));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.inner_step_container, fragment, Config.INNER_DETAILS_TAG)
                        .commit();
            }else{
                Intent intent = new Intent(this, StepDetailsActivity.class);
                intent.putExtra(Config.STEPS_INTENT,recipeHolder);
                intent.putExtra(Config.STEP_INDEX, position);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the current position & current recipe
        if (Config.TwoPane){
            outState.putInt(Config.STEP_POSITION_STATE,currentPosition);
            outState.putParcelable(Config.RECIPE_STEPS_STATE,recipeHolder);
        }
        super.onSaveInstanceState(outState);
    }

}
