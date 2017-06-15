package com.shehabsalah.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.api.GET_Connector;
import com.shehabsalah.bakingapp.api.UriBuilder;
import com.shehabsalah.bakingapp.api.parser.ParseJsonItems;
import com.shehabsalah.bakingapp.ui.activities.MainActivity;
import com.shehabsalah.bakingapp.util.Config;
import com.shehabsalah.bakingapp.util.GetInfoAsyncTask;

import org.json.JSONArray;

/**
 * Implementation of App Widget functionality.
 */
public class RecipesAppWidget extends AppWidgetProvider {



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);
        // Set the ListWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        // Set the MainActivity intent to launch when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

        // Handle empty gardens
        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Connect with the API and retrieve all Recipes from the server
        getNewRecipes(context);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context,appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    private void getNewRecipes(final Context context){
        if(Config.isNetworkConnected(context)){
            UriBuilder uriBuilder = new UriBuilder(Config.SCHEME, Config.BASE_URL, Config.DATA_SOURCE);
            GET_Connector getItemAPI = new GET_Connector(uriBuilder.getURL());
            GetInfoAsyncTask getInfoAsyncTask = new GetInfoAsyncTask() {
                @Override
                protected void onPreExecute() {
                }

                @Override
                protected void onPostExecute(JSONArray jsonArray) {
                    if (jsonArray != null){
                        ParseJsonItems parseJsonItems = new ParseJsonItems(context,jsonArray);
                        parseJsonItems.parseRecipe();
                    }
                }
            };
            getInfoAsyncTask.execute(getItemAPI);
        }
    }
}

