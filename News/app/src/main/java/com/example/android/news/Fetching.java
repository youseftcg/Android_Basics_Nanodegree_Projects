package com.example.android.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static com.example.android.news.MainActivity.LOG_TAG;

/**
 * Created by Yousef on 10/27/2017.
 */

// Class for Fetching data
public class Fetching {

    public static ArrayList<NewsObject> fetchData(String link) {

        if (link == null || link.isEmpty()) {
            return null;
        }
        URL url = createUrl(link);

        String data = makeHttpRequest(url);


        ArrayList<NewsObject> finalData = parseJSON(data);
        Log.e(LOG_TAG, "I'm after parsing = " + finalData);


        return finalData;
    }

    // to Create the URL from the String Link
    private static URL createUrl(String link) {
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem creating URL");
        }
        return url;
    }

    // To open the URL and to get data from
    private static String makeHttpRequest(URL url) {
        if (url == null) {
            return null;
        }

        InputStream inputStream = null;
        HttpURLConnection httpsURLConnection = null;
        String data = null;

        try {
            httpsURLConnection = (HttpURLConnection) url.openConnection();

            httpsURLConnection.setRequestMethod("GET");

            httpsURLConnection.setReadTimeout(15000);
            httpsURLConnection.setConnectTimeout(15000);

            httpsURLConnection.connect();


            // if connection is valid get data
            if (httpsURLConnection.getResponseCode() == 200) {
                inputStream = httpsURLConnection.getInputStream();
                data = readInput(inputStream);
            } else {
                Log.e(LOG_TAG, "Error with response Code , it is" + httpsURLConnection.getResponseCode());

            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request");
            // at the end close everything , whether it was success or not
        } finally {

            if (httpsURLConnection != null)
                httpsURLConnection.disconnect();

            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return data;
    }

    // Helper method to be called by makeHttpRequest to decode the data (input stream)
    private static String readInput(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    // to Parse the JSON data from makeHttpRequest and return it as an ArrayList<NewsObject>
    public static ArrayList<NewsObject> parseJSON(String data) {

        if (data == null) {
            return null;
        }
        // Create the ArrayList that data will be stored on it
        ArrayList<NewsObject> finalArray = new ArrayList<>();

        try {
            JSONObject allData = new JSONObject(data);

            if (allData.optInt("num_results") == 0) {
                return null;
            } else {
                JSONArray jsonArray = allData.optJSONArray("results");

                // Iterate over each item to parse data

                //Probleem after item 12
                for (int i = 0; i < 5; i++) {

                    String publishedDate = null;
                    InputStream inputStream = null;
                    Bitmap photo = null;
                    String title = null;
                    String url = null;

                    JSONObject item = jsonArray.getJSONObject(i);

                    if (item.has("published_date")) {
                        //Getting published date of the article
                        publishedDate = item.getString("published_date");
                    }
                    if (item.has("title")) {
                        //Getting the title of the article
                        title = item.optString("title");
                    }
                    if (item.has("url")) {
                        //Getting the url of the article
                        url = item.optString("url");
                    }
                    if (item.has("multimedia")) {
                        // Getting the image of the article header
                        JSONArray media = item.optJSONArray("multimedia");
                        if (media.length() >= 3) {
                            JSONObject photoDetails = media.optJSONObject(2);
                            String photoLink = photoDetails.optString("url");
                            if(photoLink.endsWith(".jpg")) {
                                // decode the photo if it's a JPG formatted
                                URL photoUrl = createUrl(photoLink);

                                try {
                                    inputStream = photoUrl.openStream();
                                    photo = BitmapFactory.decodeStream(inputStream);

                                } catch (IOException e) {
                                    Log.e(LOG_TAG, "Problem decoding the photo");
                                }
                            }
                        }
                    }
                    finalArray.add(new NewsObject(photo, publishedDate, title, url));
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON data");
        }

        return finalArray;
    }
}
