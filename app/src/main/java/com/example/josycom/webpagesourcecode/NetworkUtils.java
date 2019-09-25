package com.example.josycom.webpagesourcecode;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    static String getSourceCode(String urlInput){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String codeJSONString = null;
        try {
            Uri builtURI = Uri.parse(urlInput).buildUpon().build();
            URL requestUrl = new URL(builtURI.toString());
            urlConnection =(HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Get the InputStream
            InputStream inputStream = urlConnection.getInputStream();
            // Create a buffered reader from that input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Use a StringBuilder to hold the incoming response
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }
            // If Stream is empty, there is no point parsing
            if (builder.length() == 0){
                return null;
            }
            codeJSONString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, codeJSONString);
        return codeJSONString;
    }
}
