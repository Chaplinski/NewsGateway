package com.example.newsgateway;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class AsyncNewsSourceLoader extends AsyncTask<String, Integer, String> {

    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;

    private static final String dataURL = "https://newsapi.org/v2/sources";
    private static final String yourAPIKey = "c5a211b9b58a4a7f911fdde19fe9b1e2";

    AsyncNewsSourceLoader(MainActivity ma) {
        mainActivity = ma;
    }


    @Override
    protected void onPostExecute(String s) {
        ArrayList<Source> sourceList = parseJSON(s);
        if (sourceList != null) {
            mainActivity.updateData(sourceList);
        }
    }


    @Override
    protected String doInBackground(String... params) {
        String sLocalURL = dataURL;
        Uri.Builder buildURL = Uri.parse(sLocalURL).buildUpon();

        buildURL.appendQueryParameter("language", "en");
        buildURL.appendQueryParameter("country", "us");
        buildURL.appendQueryParameter("category", "");
        buildURL.appendQueryParameter("apiKey", yourAPIKey);
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground2: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            Log.d(TAG, "doInBackground: beginning of try");

            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            Log.d(TAG, "doInBackground: before while");

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: end of try");
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: in catch");
            e.printStackTrace();
            return null;
        }
        Log.d(TAG, "doInBackground: " + sb.toString());

        return sb.toString();
    }


    private ArrayList<Source> parseJSON(String s) {

        ArrayList<Source> sourceList = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray jSources = (JSONArray) jObjMain.get("sources");


            for (int i = 0; i < jSources.length(); i++) {
                JSONObject jThisSource = (JSONObject) jSources.get(i);
                String id = jThisSource.getString("id");
                String name = jThisSource.getString("name");
                String category = jThisSource.getString("category");


                sourceList.add(
                        new Source(id, name, category));

            }
            Log.d(TAG, "parseJSON: success");
            return sourceList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
