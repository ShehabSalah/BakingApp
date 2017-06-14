package com.shehabsalah.bakingapp.util;

import org.json.JSONArray;

/**
 * Created by ShehabSalah on 6/7/17.
 * Get info interface must implement in classes that will communicate with the backend
 */
public interface GetInfo {
    /**
     * getInfo Abstract method responsible on the communication with the backend
     * @return JSONArray: the backend response
     * */
    JSONArray getInfo();
}

