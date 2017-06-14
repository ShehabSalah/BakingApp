package com.shehabsalah.bakingapp.api.parser;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.shehabsalah.bakingapp.data.RecipeHolder;
import com.shehabsalah.bakingapp.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by ShehabSalah on 6/7/17.
 * This class responsible on paring the items
 */
public class ParseJsonItems {
    private JSONArray jsonArray;
    private Set<String> recipeSet;
    SharedPreferences pref;

    /**
     * Initializing variables needed to perform the parsing
     * @param context Context on the application to save recipes in SharedPreferences
     * @param jsonArray JsonArray of the content to parse
     * */
    public ParseJsonItems(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        pref = context.getSharedPreferences(Config.P_TAG, Context.MODE_PRIVATE);
        recipeSet = new LinkedHashSet<>();

    }
    /**
     * Paring the given JsonArray and save the recipes names in SharedPreferences
     * @return ArrayList of RecipeHolder holding recipes information parsed by the given JsonArray
     */
    public ArrayList<RecipeHolder> parseRecipe(){
        // initialize ArrayList of RecipeHolder to hold recipes information parsed by the given JsonArray
        ArrayList<RecipeHolder> recipeHolders = new ArrayList<>();
        //Get object of Gson() class
        Gson gson = new Gson();
        try{
            // Parse the json
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject itemInfo = jsonArray.getJSONObject(i);
                recipeHolders.add(gson.fromJson(itemInfo.toString(),RecipeHolder.class));
                // Add copy of recipe name to recipeSet, used later in application widget
                recipeSet.add(recipeHolders.get(i).getName());
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        // Save recipeSet that contain copy of recipes names in SharedPreferences
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(Config.P_LIST_KEY, recipeSet);
        editor.apply();
        return recipeHolders;
    }
}