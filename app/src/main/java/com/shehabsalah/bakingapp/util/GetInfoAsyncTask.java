package com.shehabsalah.bakingapp.util;

import android.os.AsyncTask;

import org.json.JSONArray;

/**
 * Created by ShehabSalah on 6/7/17.
 * This class responsible on retrieving information's from the server side
 */
public abstract class GetInfoAsyncTask extends AsyncTask<GetInfo,Void,JSONArray> {
    @Override
    protected JSONArray doInBackground(GetInfo... params) {
        return params[0].getInfo();
    }

    @Override
    protected abstract void onPreExecute();

    @Override
    protected abstract void onPostExecute(JSONArray jsonArray);
}
