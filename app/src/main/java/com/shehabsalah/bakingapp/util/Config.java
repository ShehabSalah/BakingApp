package com.shehabsalah.bakingapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Parcelable;

/**
 * Created by ShehabSalah on 6/7/17.
 * General Configuration
 */

public class Config {
    // API Constants
    public static final String SCHEME                   = "https";
    public static final String BASE_URL                 = "d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking";
    public static final String DATA_SOURCE              = "baking.json";

    // Fragment Tags
    public static final String MAIN_FRAGMENT_TAG        = "recipe_fragment";
    public static final String DETAILS_FRAGMENT_TAG     = "recipe_details_fragment";
    public static final String INNER_DETAILS_TAG        = "step_tag";

    // Saved States
    public static final String RECIPE_STATE_TAG         = "001";
    public static final String RECIPE_DETAILS_STATE_TAG = "002";
    public static final String STEP_STATE               = "003";
    public static final String INGR_STATE               = "004";
    public static final String WINDOW_STATE             = "005";
    public static final String POSITION_STATE           = "006";
    public static final String RESTORE_STATE            = "007";
    public static final String BUNUEL_STATE             = "008";
    public static final String STEP_POSITION_STATE      = "009";
    public static final String RECIPE_STEPS_STATE       = "010";
    public static final String DETAILS_LIST_STATE       = "011";
    public static final String GRID_STATE               = "012";
    public static Parcelable RECIPE_STATE               = null;

    // Two Pane
    public static boolean TwoPane                       = false;
    public static int DetailsSelected                   = -1;

    // Intents
    public static final String RECIPE_ITEM              = "recipe_intent";
    public static final String STEPS_INTENT             = "steps_intent";
    public static final String STEP_INDEX               = "step_index";
    public static final String P_TAG                    = "p_tag";
    public static final String P_LIST_KEY               = "p_key";
    public static final String WIDGET_INTENT            = "widget_intent";
    public static final String WIDGET_GET_IN            = "getIngredient";

    //Check internet connection
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}