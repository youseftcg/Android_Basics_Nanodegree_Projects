package com.example.android.booklisting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.R.attr.author;
import static android.R.attr.icon;
import static android.R.id.input;
import static android.os.Build.VERSION_CODES.M;
import static com.example.android.booklisting.MainActivity.LOG_TAG;

/**
 * Created by Yousef on 10/13/2017.
 */

public class FetchingData {

    public static ArrayList<ContainerObject> getData(String link) {
        Log.e(LOG_TAG, "I'm in Fetching data before if");

        if (link == null || link.isEmpty()) {
            return null;
        }

        Log.e(LOG_TAG, "I'm in Fetching data after if");
        //Create a URL from the String
        URL url = CreatUrl(link);

        Log.e(LOG_TAG, "I'm in Fetching data after creating url" + url);
        // Connect to the internet using the URL and get the data
        String data = MakeHttpRequest(url);

        // Parse the JSON data
        return parseJson(data);
    }

    //convert the String into URL
    public static URL CreatUrl(String link) {
        if (link == null)
            return null;

        URL url = null;

        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating Url", e);
        }
        return url;
    }

    public static String MakeHttpRequest(URL url) {
        if (url == null) {
            return null;
        }

        Log.e(LOG_TAG, "I'm after if in MakeHttpRequest ");

        HttpsURLConnection httpsURLConnection = null;
        InputStream inputStream = null;
        String result = null;


        try {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");

            httpsURLConnection.setConnectTimeout(10000);
            httpsURLConnection.setReadTimeout(15000);

            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() == 200) {
                inputStream = httpsURLConnection.getInputStream();
                result = readInput(inputStream);
                Log.e(LOG_TAG, "Response code is 200");
            } else {
                Log.e(LOG_TAG, "Error, response code is: " + httpsURLConnection.getResponseCode());

            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with openning connection ", e);
        } finally {
            if (httpsURLConnection != null)
                httpsURLConnection.disconnect();

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static String readInput(InputStream input) {
        if (input == null)
            return null;

        StringBuilder stringBuilder = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(input, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with Reading data (readLine) ", e);
        }
        return stringBuilder.toString();
    }

    public static ArrayList<ContainerObject> parseJson(String JSONUrl) {

        ArrayList<ContainerObject> finalJsonArray = new ArrayList<>();

        if (JSONUrl == null)
            return null;

        try {
            // Make an JSONObject from the whole JSON data
            JSONObject jsonObject = new JSONObject(JSONUrl);
            //Check if there are items to get , and if not return null
            if (jsonObject.optInt("totalItems") == 0) {
                return null;
            }
            // ArrayList to collect the final data from the for loop

            // Iterate for each object in the List and make an array to get the items from the JSON data to then be able to retrieve them
            JSONArray jsonArray = jsonObject.optJSONArray("items");


            for (int i = 0; jsonArray.length() > i; i++) {
                String title = null, author = null, datePublished = null;
                URL imageUrl = null;
                Bitmap icon = null;
                //Initialize  variables to recall at the end to add them to the ArrayList

                // get the current position of the object in the list.

                JSONObject iterateObject = jsonArray.optJSONObject(i);
                JSONObject loopObject = iterateObject.optJSONObject("volumeInfo");

                if (loopObject.has("title")) {
                    title = loopObject.optString("title");
                    Log.e(LOG_TAG, "After if Title is: " + title);

                }
                if (loopObject.has("authors")) {
                    JSONArray authorsArray = loopObject.optJSONArray("authors");
                    author = authorsArray.optString(0);

                    Log.e(LOG_TAG, "After if Authors is: " + author);

                }
                if (loopObject.has("publishedDate")) {
                    datePublished = loopObject.optString("publishedDate");

                    Log.e(LOG_TAG, "After if DataPublished is: " + datePublished);

                }

                if (loopObject.has("imageLinks")) {
                    String imageString = null;

                    JSONObject image = loopObject.optJSONObject("imageLinks");
                    Log.e(LOG_TAG, "After if URL is: " + image);

                    if (image.has("smallThumbnail")) {
                        imageString = image.optString("smallThumbnail");
                    } else if (image.has("thumbnail")) {
                        imageString = image.optString("thumbnail");
                    }
                    imageUrl = CreatUrl(imageString);
                    InputStream in = imageUrl.openStream();
                    icon = BitmapFactory.decodeStream(in);
                }
                Log.e(LOG_TAG, "Fetching data , JSON after parsing : " + title + author + datePublished);

                finalJsonArray.add(new ContainerObject(icon, title, author, datePublished));
                Log.e(LOG_TAG, "Finished FOR loop");

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error with parsing JSON data ", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with decoding photo ", e);
        }
        return finalJsonArray;

    }
}