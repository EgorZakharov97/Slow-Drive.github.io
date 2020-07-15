package com.Logic.Core;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.CustomExceptions.HTTPHandlerException;


import com.Logic.Object.CLocation;
import com.Logic.Static.HTTPSHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Request {
    private static String key;
    private static String url;
    // CONSTANTS
    private static final String TAG = "REQUEST";
    private static final String TRAVEL_MODE = "driving";
    private static final boolean TRUCK_SPEED_LIMIT = false;

    // VARIABLES
    private static Location defaultLocation;
    //setter
    public static void setAPI(String url1,String key1){
        url=url1;
        key=key1;
    }
    // SPEED LIMIT REQUEST
    public static void makeRequest(Location location) throws Exception {
        defaultLocation = location;

        new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String response = "";
                try {
                    //GET Request
                    URL url = constructURL();
                    response = HTTPSHandler.sendGet(url);
                } catch (HTTPHandlerException e){
                    e.printStackTrace();
                } catch (MalformedURLException e){
                    e.printStackTrace();
                }
                return response;
            }

            // This will send the response to a class that called Request.setContext() the last.
            @Override
            protected void onPostExecute(String s) {
                CLocation userLocation = parseResponse(s);
                LocationHandler.onRequestFinished(userLocation);
            }

            //    constructs a request URL with required parameters
            private URL constructURL() throws MalformedURLException {
                if(key!=null&&url!=null) {
                    String str = url + "points=" + defaultLocation.getLatitude() + "," + defaultLocation.getLongitude();
                    str += "&includeTruckSpeedLimit=" + TRUCK_SPEED_LIMIT;
                    str += "&IncludeSpeedLimit=true";
                    str += "&speedUnit=MPH";
                    str += "&travelMode=" + TRAVEL_MODE;
                    str += "&key=" + key;

                    return new URL(str);
                }
                else{
                    Log.e("Missing","Api url or key is not set up");
                    return null;
                }
            }

            // Gets the raw string and creates a data object
            private CLocation parseResponse(String rawData) {
                JSONObject data;
                CLocation userLocation;
                try {
                    data = new JSONObject(rawData);
                    Log.d("JSON::all", data.toString());
                    JSONObject resources = (JSONObject) ((JSONArray) data.get("resourceSets")).get(0);
                    Log.d("JSON::resources", resources.toString());
                    JSONObject res = (JSONObject) ((JSONArray) resources.get("resources")).get(0);
                    Log.d("JSON::res", res.toString());
                    JSONObject point = (JSONObject) ((JSONArray) res.get("snappedPoints")).get(0);
                    Log.d("JSON::point", point.toString());

                    double latitude = point.getJSONObject("coordinate").getDouble("latitude");
                    double longitude = point.getJSONObject("coordinate").getDouble("longitude");
                    String streetName = point.getString("name");
                    int speedLimit = point.getInt("speedLimit");
                    String speedUnit = point.getString("speedUnit");

                    userLocation = new CLocation(defaultLocation, speedLimit, streetName);
                    Log.d(TAG, "LocationData object created:\nCurrent speed limit: " + userLocation.getSpeedLimit() + "\nStreet name: " + userLocation.getStreetName());

                } catch (JSONException e) {
                    e.printStackTrace();
                    userLocation = new CLocation(defaultLocation);
                }
                return userLocation;
            }
        }.execute();
    }
}
