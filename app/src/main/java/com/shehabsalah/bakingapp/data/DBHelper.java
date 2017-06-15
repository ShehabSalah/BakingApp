package com.shehabsalah.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shehabsalah.bakingapp.data.ingredient.IngredientColumn;
import com.shehabsalah.bakingapp.data.recipes.RecipeColumn;

/**
 * Created by Shehab Salah on 6/14/17.
 * This Class is responsible on creating the Database schema and it's Tables
 */
public class DBHelper extends SQLiteOpenHelper {
    //DATABASE_VERSION is containing the version number of the current DB schema.
    //If the Database schema changed, the DATABASE_VERSION must change also.
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "backingApp.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Recipe Table
        final String CREATE_RECIPE_TABLE            = "CREATE TABLE " + RecipeColumn.TABLE_NAME + " ( " +
                RecipeColumn._ID                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeColumn.COL_RECIPE_ID          + " INTEGER NOT NULL, " +
                RecipeColumn.COL_RECIPE_NAME        + " TEXT NOT NULL, " +
                "UNIQUE ("                          + RecipeColumn.COL_RECIPE_ID + ") ON CONFLICT REPLACE);";

        //Create Ingredient Table
        final String CREATE_INGREDIENT_TABLE        = "CREATE TABLE " + IngredientColumn.TABLE_NAME + " ( " +
                IngredientColumn._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientColumn.COL_RECIPE_ID      + " INTEGER NOT NULL, " +
                IngredientColumn.COL_QUANTITY       + " REAL NOT NULL, " +
                IngredientColumn.COL_MEASURE        + " TEXT NOT NULL, " +
                IngredientColumn.COL_INGREDIENT     + " TEXT NOT NULL, " +
                " FOREIGN KEY ( "                   + IngredientColumn.COL_RECIPE_ID + " ) REFERENCES " +
                RecipeColumn.TABLE_NAME             + "( " + RecipeColumn.COL_RECIPE_ID + " )); ";

        //Execute the SQL of creating table recipe
        db.execSQL(CREATE_RECIPE_TABLE);
        //Execute the SQL of creating table ingredient
        db.execSQL(CREATE_INGREDIENT_TABLE);
    }

    //This method is Called only if the
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop database tables DATABASE_VERSION number is changed
        db.execSQL("DROP TABLE IF EXISTS " + RecipeColumn.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredientColumn.TABLE_NAME);
        //Create New Database
        onCreate(db);

    }
}
