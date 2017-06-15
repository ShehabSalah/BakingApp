package com.shehabsalah.bakingapp.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Shehab Salah on 6/12/17.
 *
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}