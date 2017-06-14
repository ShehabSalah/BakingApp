package com.shehabsalah.bakingapp.api;

import com.shehabsalah.bakingapp.util.GetInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ShehabSalah on 6/7/17.
 * This class responsible on connecting the application with GET web services and return Json File
 */
public class GET_Connector implements GetInfo {
    private URL _url;

    /**
     * Initialize Main URL
     * @param _url Main URL that the application will communicate with.
     * */
    public GET_Connector(URL _url) {
        this._url = _url;
    }

    @Override
    public JSONArray getInfo() {
        String listJsonStr = null;
        try{
            // Create the request to the API, and open the connection
            HttpURLConnection urlConnection = (HttpURLConnection) _url.openConnection();
            String method = "GET";
            urlConnection.setRequestMethod(method);
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    //append each line from input stream into String Buffer
                    buffer.append(line + "\n");
                }
                //if buffer contain data
                if (buffer.length() != 0) {
                    //Convert the StringBuffer to String
                    listJsonStr = buffer.toString();
                }
                //Convert the JsonString to JsonObject
                if (listJsonStr != null)
                    return new JSONArray(listJsonStr);
            }
        }catch(IOException | JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}