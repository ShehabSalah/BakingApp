package com.shehabsalah.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.shehabsalah.bakingapp.data.ingredient.IngredientColumn;
import com.shehabsalah.bakingapp.data.recipes.RecipeColumn;
import com.shehabsalah.bakingapp.util.Config;

/**
 * Created by Shehab Salah on 6/14/17.
 * This class is responsible on separating the underline Data Storage from the Application.
 * This Class also responsible on making the underline Data Storage of the Application shared
 * among all other applications
 */

public class RecipeProvider extends ContentProvider {
    private SQLiteDatabase db;
    //Recipe Uri Number
    private static final int TYPE_RECIPE = 0;
    //Ingredient Uri Number
    private static final int TYPE_INGREDIENT = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    //Match each previous number with the corresponding URI
    static {
        URI_MATCHER.addURI(Config.AUTHORITY, RecipeColumn.TABLE_NAME, TYPE_RECIPE);
        URI_MATCHER.addURI(Config.AUTHORITY, IngredientColumn.TABLE_NAME, TYPE_INGREDIENT);
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }
    @Override
    public Cursor query(@Nullable Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Switch the coming Uri with its corresponding number
        int match = URI_MATCHER.match(uri);
        //Return the cursor result based on the uri number
        switch (match) {
            case TYPE_RECIPE:
                return db.query(
                        RecipeColumn.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            case TYPE_INGREDIENT:
                return db.query(
                        IngredientColumn.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        IngredientColumn._ID + " ASC"
                );

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
    @Override
    public String getType(@Nullable Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case TYPE_RECIPE:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + RecipeColumn.TABLE_NAME;
            case TYPE_INGREDIENT:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + IngredientColumn.TABLE_NAME;
        }
        return null;
    }
    @Override
    public Uri insert(@Nullable Uri uri, ContentValues values) {
        //Switch the coming Uri with its corresponding number
        int match = URI_MATCHER.match(uri);
        //Database row id
        long rowID = 0;
        //This Uri is changed based on the inserted Database Table
        Uri _uri = null;
        switch (match) {
            case TYPE_RECIPE:
                rowID = db.insert(RecipeColumn.TABLE_NAME, "", values);
                _uri = RecipeColumn.CONTENT_URI;
                break;
            case TYPE_INGREDIENT:
                rowID = db.insert(IngredientColumn.TABLE_NAME, "", values);
                _uri = IngredientColumn.CONTENT_URI;
                break;
        }
        if (rowID > 0) {
            Uri _uri2 = ContentUris.withAppendedId(_uri, rowID);
            getContext().getContentResolver().notifyChange(_uri2, null);
            return _uri;
        } else {
            return null;
        }
    }
    @Override
    public int delete(@Nullable Uri uri, String selection, String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        int count = 0;
        switch (match) {
            case TYPE_RECIPE:
                count = db.delete(RecipeColumn.TABLE_NAME, selection, selectionArgs);
                break;
            case TYPE_INGREDIENT:
                count = db.delete(IngredientColumn.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    @Override
    public int update(@Nullable Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int match = URI_MATCHER.match(uri);
        int count = 0;
        switch (match) {
            case TYPE_RECIPE:
                count = db.update(RecipeColumn.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TYPE_INGREDIENT:
                count = db.update(IngredientColumn.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
