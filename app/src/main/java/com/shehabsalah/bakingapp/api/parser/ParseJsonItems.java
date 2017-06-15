package com.shehabsalah.bakingapp.api.parser;

import android.content.Context;

import com.google.gson.Gson;
import com.shehabsalah.bakingapp.data.ingredient.IngredientColumn;
import com.shehabsalah.bakingapp.data.recipes.RecipeColumn;
import com.shehabsalah.bakingapp.data.recipes.RecipeHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ShehabSalah on 6/7/17.
 * This class responsible on paring the items
 */
public class ParseJsonItems {
    private JSONArray jsonArray;
    private Context context;

    /**
     * Initializing variables needed to perform the parsing
     * @param context Context on the application to save recipes in SharedPreferences
     * @param jsonArray JsonArray of the content to parse
     * */
    public ParseJsonItems(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;

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
        IngredientColumn.delete(context);
        RecipeColumn.delete(context);
        try{
            // Parse the json
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject itemInfo = jsonArray.getJSONObject(i);
                recipeHolders.add(gson.fromJson(itemInfo.toString(),RecipeHolder.class));
                RecipeColumn.insert(context, recipeHolders.get(i));
                IngredientColumn.insert(context, recipeHolders.get(i));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return recipeHolders;
    }
}