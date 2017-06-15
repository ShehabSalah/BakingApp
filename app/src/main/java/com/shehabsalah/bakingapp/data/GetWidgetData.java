package com.shehabsalah.bakingapp.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.data.ingredient.Ingredient;
import com.shehabsalah.bakingapp.data.ingredient.IngredientColumn;
import com.shehabsalah.bakingapp.data.recipes.RecipeColumn;
import com.shehabsalah.bakingapp.data.recipes.RecipeHolder;
import com.shehabsalah.bakingapp.util.Config;

import java.util.ArrayList;

/**
 * Created by Shehab Salah on 6/15/17.
 *
 */

public class GetWidgetData {
    private Cursor recipeCursor;
    private Cursor ingredientCursor;

    private ArrayList<RecipeHolder> getArrayList(Context context) {
        ArrayList<RecipeHolder> recipeHolders = new ArrayList<>();
        if (recipeCursor != null) recipeCursor.close();
        recipeCursor = context.getContentResolver().query(
                RecipeColumn.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (recipeCursor != null && recipeCursor.moveToFirst()){
            do {
                int recipeID = recipeCursor.getInt(recipeCursor.getColumnIndex(RecipeColumn.COL_RECIPE_ID));
                String recipeName = recipeCursor.getString(recipeCursor.getColumnIndex(RecipeColumn.COL_RECIPE_NAME));

                recipeHolders.add(new RecipeHolder(recipeID, recipeName, getRecipeIngredient(context, recipeID), null, -1, null));
            }while (recipeCursor.moveToNext());
            recipeCursor.close();
        }else
            return null;
        return recipeHolders;
    }

    private ArrayList<Ingredient> getRecipeIngredient(Context context, int recipeID){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        if (ingredientCursor != null) ingredientCursor.close();
        ingredientCursor = context.getContentResolver().query(
                IngredientColumn.CONTENT_URI,
                null,
                IngredientColumn.COL_RECIPE_ID + " = ?",
                new String[]{Integer.toString(recipeID)},
                null
        );
        if (ingredientCursor != null && ingredientCursor.moveToFirst()){
            do {
                double quantity = ingredientCursor.getDouble(ingredientCursor.getColumnIndex(IngredientColumn.COL_QUANTITY));
                String measure = ingredientCursor.getString(ingredientCursor.getColumnIndex(IngredientColumn.COL_MEASURE));
                String ingredient = ingredientCursor.getString(ingredientCursor.getColumnIndex(IngredientColumn.COL_INGREDIENT));
                ingredients.add(new Ingredient(quantity, measure, ingredient));
            }while (ingredientCursor.moveToNext());
            ingredientCursor.close();
        }
        return ingredients;
    }

    public ArrayList<RemoteViews> getRemoteViews(Context context){

        ArrayList<RecipeHolder> recipeHolders = getArrayList(context);
        ArrayList<RemoteViews> remoteViewsArrayList = new ArrayList<>();

        RemoteViews ingredient_item = null;
        if (recipeHolders == null) return null;
        for (int position = 0; position < recipeHolders.size(); position++){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            remoteViews.removeAllViews(R.id.parent_layout);

            RemoteViews listHeader = new RemoteViews(context.getPackageName(), R.layout.widget_list_header);
            listHeader.setTextViewText(R.id.widget_name, recipeHolders.get(position).getName());
            remoteViews.addView(R.id.parent_layout, listHeader);

            for (int i = 0; i < recipeHolders.get(position).getIngredients().size(); i++){
                ingredient_item = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient);
                ingredient_item.setTextViewText(R.id.quantity, Double.toString(recipeHolders.get(position).getIngredients().get(i).getQuantity()));
                ingredient_item.setTextViewText(R.id.measure, recipeHolders.get(position).getIngredients().get(i).getMeasure());
                ingredient_item.setTextViewText(R.id.ingredient, recipeHolders.get(position).getIngredients().get(i).getIngredient());
                remoteViews.addView(R.id.parent_layout, ingredient_item);
            }

            Intent appIntent = new Intent();
            appIntent.putExtra(Config.WIDGET_INTENT, recipeHolders.get(position).getName());
            remoteViews.setOnClickFillInIntent(R.id.parent_layout, appIntent);
            remoteViewsArrayList.add(remoteViews);
        }
        return remoteViewsArrayList;
    }
}
