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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;


public class AsyncStoryLoader extends AsyncTask<String, Integer, String> {

    @SuppressLint("StaticFieldLeak")
    private NewsService.ServiceReceiver serviceReceiver;

    private static final String dataURL = "https://newsapi.org/v2/top-headlines";
    private static final String yourAPIKey = "c5a211b9b58a4a7f911fdde19fe9b1e2";
    private String sSourceId;

    AsyncStoryLoader(NewsService.ServiceReceiver sr, String sSourceId) {
        serviceReceiver = sr;
        this.sSourceId = sSourceId;
    }


    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: herre");
        ArrayList<Story> storyList = parseJSON(s);
        Log.d(TAG, "onPostExecute: " + storyList.size());
        if (storyList != null) {
            serviceReceiver.updateData(storyList);
        }
    }


    @Override
    protected String doInBackground(String... params) {
        String sLocalURL = dataURL;
        Uri.Builder buildURL = Uri.parse(sLocalURL).buildUpon();

        buildURL.appendQueryParameter("sources", sSourceId);
        buildURL.appendQueryParameter("language", "en");
        buildURL.appendQueryParameter("apiKey", yourAPIKey);
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground5: " + urlToUse);

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


    private ArrayList<Story> parseJSON(String s) {

        ArrayList<Story> storyList = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray jArticles = (JSONArray) jObjMain.get("articles");


            for (int i = 0; i < jArticles.length(); i++) {
                JSONObject jThisSource = (JSONObject) jArticles.get(i);
                String author = jThisSource.getString("author");
                String title = jThisSource.getString("title");
                String description = jThisSource.getString("description");
                String url = jThisSource.getString("url");
                String urlToImage = jThisSource.getString("urlToImage");
                String publishedAt = jThisSource.getString("publishedAt");

//                DateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US);
//                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
//                Date date = inputFormat.parse(publishedAt);
//                String outputText = outputFormat.format(date);
//
//                Log.d(TAG, "parseJSON: " + outputText);

                storyList.add(
                        new Story(author, title, description, url, urlToImage, publishedAt));

            }
            Log.d(TAG, "parseJSON: " + storyList.size());
            return storyList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON2: " + e);
            e.printStackTrace();
        }
        return null;
    }


}
