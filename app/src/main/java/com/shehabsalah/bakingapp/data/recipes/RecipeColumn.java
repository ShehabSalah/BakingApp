package com.shehabsalah.bakingapp.data.recipes;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.shehabsalah.bakingapp.util.Config;

/**
 * Created by Shehab Salah on 6/14/17.
 * This Class contain the ContentProvider URI's and Database Column names of the Recipes
 */

public class RecipeColumn implements BaseColumns {
    public static final String TABLE_NAME = "recipe";
    //General Content URI
    public static final Uri CONTENT_URI = Config.CONTENT_URI_BASE.buildUpon().appendPath(TABLE_NAME).build();

    //Table Columns
    public static final String COL_RECIPE_ID            = "recipe_id";
    public static final String COL_RECIPE_NAME          = "recipe_name";

    public static void insert(Context context, RecipeHolder item) {
        RecipeContentValues recipeContentValues = new RecipeContentValues();
        context.getContentResolver().insert(
                CONTENT_URI,
                recipeContentValues.contentValues_NewRecipe(item.getId(), item.getName())
        );
    }

    public static void delete(Context context){
        context.getContentResolver().delete(CONTENT_URI,null,null);
    }
}
