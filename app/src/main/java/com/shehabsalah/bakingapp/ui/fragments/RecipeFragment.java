package com.shehabsalah.bakingapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.api.GET_Connector;
import com.shehabsalah.bakingapp.api.UriBuilder;
import com.shehabsalah.bakingapp.api.parser.ParseJsonItems;
import com.shehabsalah.bakingapp.data.recipes.RecipeHolder;
import com.shehabsalah.bakingapp.ui.activities.RecipeDetailsActivity;
import com.shehabsalah.bakingapp.ui.adapters.RecipeListAdapter;
import com.shehabsalah.bakingapp.util.Config;
import com.shehabsalah.bakingapp.util.GetInfoAsyncTask;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShehabSalah on 6/7/17.
 * RecipeFragment responsible on displaying the Recipes
 */

public class RecipeFragment extends Fragment {
    @BindView(R.id.recipe_list) GridView gridView;

    //private final String LOG_TAG = RecipeFragment.class.getSimpleName();

    private ProgressDialog pDialog;
    ArrayList<RecipeHolder> recipeHolders;


    public RecipeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.recipe_list, container, false);
        ButterKnife.bind(this, mainView);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(true);
        if(savedInstanceState == null || !savedInstanceState.containsKey(Config.RECIPE_STATE_TAG)){
            recipeLoader();
        } else{
            recipeHolders = savedInstanceState.getParcelableArrayList(Config.RECIPE_STATE_TAG);
            restoreViews(recipeHolders, savedInstanceState.getParcelable(Config.GRID_STATE));
        }

        return mainView;
    }

    private void recipeLoader(){
        if(Config.isNetworkConnected(getActivity())){
            UriBuilder uriBuilder = new UriBuilder(Config.SCHEME, Config.BASE_URL, Config.DATA_SOURCE);
            GET_Connector getItemAPI = new GET_Connector(uriBuilder.getURL());
            GetInfoAsyncTask getInfoAsyncTask = new GetInfoAsyncTask() {
                @Override
                protected void onPreExecute() {
                    pDialog.setMessage(getResources().getString(R.string.Loading_message));
                    showDialog();
                }

                @Override
                protected void onPostExecute(JSONArray jsonArray) {
                    if (jsonArray != null){
                        ParseJsonItems parseJsonItems = new ParseJsonItems(getActivity(),jsonArray);
                        bindViews(parseJsonItems.parseRecipe());

                    }else{
                        Toast.makeText(getActivity(),R.string.no_data,Toast.LENGTH_LONG).show();
                    }
                    hideDialog();
                }
            };
            getInfoAsyncTask.execute(getItemAPI);

        }else{
            Toast.makeText(getActivity(),R.string.no_internet,Toast.LENGTH_LONG).show();
        }
    }

    private void restoreViews(final ArrayList<RecipeHolder> recipeHolders, Parcelable state){
        if (recipeHolders != null && gridView!=null){
            RecipeListAdapter mAdapter = new RecipeListAdapter(recipeHolders, getActivity());
            gridView.setAdapter(mAdapter);
            gridView.onRestoreInstanceState(state);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (recipeHolders.get(i)!=null){
                        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
                        intent.putExtra(Config.RECIPE_ITEM, recipeHolders.get(i));
                        startActivity(intent);
                    }
                }
            });
        }
    }
    private void bindViews(final ArrayList<RecipeHolder> recipeHolders){
        if (recipeHolders != null){
            this.recipeHolders = recipeHolders;
            RecipeListAdapter mAdapter = new RecipeListAdapter(recipeHolders, getActivity());
            gridView.setAdapter(mAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (recipeHolders.get(i)!=null){
                        Config.DetailsSelected = -1;
                        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
                        intent.putExtra(Config.RECIPE_ITEM, recipeHolders.get(i));
                        startActivity(intent);
                    }
                }
            });
            Intent intent = getActivity().getIntent();
            if (intent.hasExtra(Config.WIDGET_INTENT)){
                String recipe_name = intent.getStringExtra(Config.WIDGET_INTENT);
                for (int i = 0; i < recipeHolders.size(); i++){
                    if (recipeHolders.get(i).getName().equals(recipe_name)){
                        Intent getIngredient = new Intent(getActivity(), RecipeDetailsActivity.class);
                        getIngredient.putExtra(Config.WIDGET_GET_IN, recipeHolders.get(i));
                        startActivity(getIngredient);
                    }
                }
            }
        }
    }

    private void showDialog() {
        if (pDialog !=null && !pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog !=null && pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (gridView!=null){
            Config.RECIPE_STATE = gridView.onSaveInstanceState();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (recipeHolders!=null && gridView != null){
            outState.putParcelableArrayList(Config.RECIPE_STATE_TAG,recipeHolders);
            outState.putParcelable(Config.GRID_STATE,gridView.onSaveInstanceState());
        }
        super.onSaveInstanceState(outState);
    }
}