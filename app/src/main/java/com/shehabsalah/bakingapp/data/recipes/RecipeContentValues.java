package com.shehabsalah.bakingapp.data.recipes;

import android.content.ContentValues;
import android.support.annotation.NonNull;

/**
 * Created by Shehab Salah on 6/14/17.
 * This Class take the (Recipe) data and convert it to ContentValues
 */

public class RecipeContentValues {

    public ContentValues contentValues_NewRecipe(@NonNull int recipe_id, @NonNull String recipe_name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeColumn.COL_RECIPE_ID, recipe_id);
        contentValues.put(RecipeColumn.COL_RECIPE_NAME, recipe_name);
        return contentValues;
    }

}
