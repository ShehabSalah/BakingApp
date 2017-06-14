package com.shehabsalah.bakingapp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.util.Config;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Shehab Salah on 6/12/17.
 *
 */

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private SharedPreferences pref;
    private Set<String> item;
    private String[] items;

    public GridRemoteViewsFactory(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(Config.P_TAG, Context.MODE_PRIVATE);
        item = pref.getStringSet(Config.P_LIST_KEY, new HashSet<String>());
        items = item.toArray(new String[item.size()]);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        item = pref.getStringSet(Config.P_LIST_KEY, new HashSet<String>());
        items = item.toArray(new String[item.size()]);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (items==null) return 0;
        return items.length;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        remoteViews.setTextViewText(R.id.widget_name, items[i]);
        Intent appIntent = new Intent();
        appIntent.putExtra(Config.WIDGET_INTENT, items[i]);
        remoteViews.setOnClickFillInIntent(R.id.parent_layout, appIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
