package com.shehabsalah.bakingapp.data.ingredient;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.shehabsalah.bakingapp.data.recipes.RecipeHolder;
import com.shehabsalah.bakingapp.util.Config;

/**
 * Created by Shehab Salah on 6/14/17.
 * This Class contain the ContentProvider URI's and Database Column names of the Ingredients
 */

public class IngredientColumn implements BaseColumns {
    public static final String TABLE_NAME = "ingredient";
    public static final Uri CONTENT_URI = Config.CONTENT_URI_BASE.buildUpon().appendPath(TABLE_NAME).build();

    //Table Columns
    public static final String COL_RECIPE_ID        = "recipe_id";       //FK
    public static final String COL_QUANTITY         = "quantity";
    public static final String COL_MEASURE          = "measure";
    public static final String COL_INGREDIENT       = "ingredient";

    public static void insert(Context context, RecipeHolder recipe) {
        for (int i = 0; i < recipe.getIngredients().size(); i++){
            Ingredient ingredient = recipe.getIngredients().get(i);
            IngredientContentValues ingredientContentValues = new IngredientContentValues();
            context.getContentResolver().insert(
                    CONTENT_URI,
                    ingredientContentValues.contentValues_NewIngredient(
                            recipe.getId(),
                            ingredient.getQuantity(),
                            ingredient.getMeasure(),
                            ingredient.getIngredient()
                    ));
        }
    }

    public static void delete(Context context){
        context.getContentResolver().delete(CONTENT_URI,null,null);
    }
}
