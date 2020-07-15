package com.Logic.Static;

import android.util.Log;

import com.CustomExceptions.HTTPHandlerException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class HTTPSHandler {

    public static String sendGet(URL obj) throws HTTPHandlerException {
        try{
            Log.d("Request url:", obj.toString());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            Log.d("Response Code :: ", "" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
                BufferedReader in = new BufferedReader(new InputStreamReader( con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                return "";
            }
        }
        catch (Exception e){
            throw new HTTPHandlerException(e);
        }
    }
}
