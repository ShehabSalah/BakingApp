package com.shehabsalah.bakingapp.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.data.recipes.RecipeHolder;
import com.shehabsalah.bakingapp.ui.adapters.RecipeStepsAdapter;
import com.shehabsalah.bakingapp.util.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShehabSalah on 6/7/17.
 * RecipeDetailsFragment responsible on displaying the Recipe Steps List
 */

public class RecipeDetailsFragment extends Fragment implements RecipeStepsAdapter.ListItemClickListener{
    @BindView(R.id.recipe_details_list)
    RecyclerView recyclerView;
    RecipeHolder recipeHolder;
    LinearLayoutManager verticalLayoutManager;
    // On step selected sending the selected step to the StepFragment
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        if (Config.DetailsSelected > -1) {
            ((OnRecipeDetailsClickListener) getActivity()).onRecipeStepSelected(recipeHolder,Config.DetailsSelected);
        }
        }
    };

    // OnRecipeClickListener interface, calls a method in the host activity named onRecipeSelected
    public interface OnRecipeDetailsClickListener {
        /**
         * Display the recipe step on a given position selected
         * @param recipeHolder recipes ingredients & steps info to display
         * @param position the selected step position
         * */
        void onRecipeStepSelected(RecipeHolder recipeHolder, int position);
    }

    public RecipeDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.recipe_details_list, container, false);
        ButterKnife.bind(this, mainView);
        if(savedInstanceState == null){
            bindViews();
        } else{
            recipeHolder = savedInstanceState.getParcelable(Config.RECIPE_DETAILS_STATE_TAG);
            restoreViews(savedInstanceState.getParcelable(Config.DETAILS_LIST_STATE));
        }
        return mainView;
    }

    private void bindViews(){
        if (recipeHolder != null){
            RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(recipeHolder, getActivity(), this, true);
            verticalLayoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(verticalLayoutManager);
            //add ItemDecoration
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(recipeStepsAdapter);
            if (Config.TwoPane) {
                Config.DetailsSelected = Config.DetailsSelected != GridView.INVALID_POSITION ?
                        Config.DetailsSelected : 0;
                recyclerView.smoothScrollToPosition(Config.DetailsSelected);
            }
        }
    }
    private void restoreViews(Parcelable state){
        if (recipeHolder != null){
            RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(recipeHolder, getActivity(), this, Config.DetailsSelected);
            verticalLayoutManager = new LinearLayoutManager(getActivity());
            verticalLayoutManager.onRestoreInstanceState(state);
            recyclerView.setLayoutManager(verticalLayoutManager);
            recyclerView.setPreserveFocusAfterLayout(true);
            //add ItemDecoration
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(recipeStepsAdapter);

            if (Config.TwoPane) {
                Config.DetailsSelected = Config.DetailsSelected != GridView.INVALID_POSITION ?
                        Config.DetailsSelected : 0;
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.smoothScrollToPosition(Config.DetailsSelected);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Config.RECIPE_DETAILS_STATE_TAG,recipeHolder);
        outState.putParcelable(Config.DETAILS_LIST_STATE,verticalLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    public void addRecipe(RecipeHolder recipeHolder){
        this.recipeHolder = recipeHolder;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Config.DetailsSelected = clickedItemIndex;
        handler.sendEmptyMessage(0);
    }
}