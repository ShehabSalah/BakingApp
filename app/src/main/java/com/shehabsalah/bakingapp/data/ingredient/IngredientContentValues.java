package com.shehabsalah.bakingapp.data.ingredient;

import android.content.ContentValues;
import android.support.annotation.NonNull;

/**
 * Created by Shehab Salah on 6/14/17.
 * This Class take the (Ingredient) data and convert it to ContentValues
 */

public class IngredientContentValues {
    public ContentValues contentValues_NewIngredient(@NonNull int recipe_id, @NonNull double quantity,
                                                     @NonNull String measure, @NonNull String ingredient) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IngredientColumn.COL_RECIPE_ID, recipe_id);
        contentValues.put(IngredientColumn.COL_QUANTITY, quantity);
        contentValues.put(IngredientColumn.COL_MEASURE, measure);
        contentValues.put(IngredientColumn.COL_INGREDIENT, ingredient);
        return contentValues;
    }
}
