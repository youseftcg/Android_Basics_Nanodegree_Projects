package com.example.popularmovies;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class JSONUtils {

    public static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    public static final String ORDER_BY_POPULAR = "popular";
    public static final String ORDER_BY_TOP_RATED = "top_rated";
    public static final String API_KEY = "api_key";
    public static final String API_KEY_VALUE = "eeb9d8adf9a51eb296439ed607c6c38b";


    public static List<Movie> fetchData(String orderBy) {

        URL url = getUrl(orderBy);

        String jsonData = getData(url);

        List<Movie> movies = parseJSON(jsonData);

        return movies;
    }

    public static URL getUrl(String orderBy) {
        // Tested , Working fine
        URL url = null;
        try {
            String uriString = Uri.parse(BASE_URL).
                    buildUpon().
                    appendPath(orderBy).
                    appendQueryParameter(API_KEY, API_KEY_VALUE).
                    build().toString();
            url = new URL(uriString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getData(URL url) {
        HttpURLConnection httpURLConnection = null ;
        String data = null;
        try {

            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            Log.e("JSONUtils", " InputStream = " + inputStream);

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if(scanner.hasNext()){
                data = scanner.next();
            }
            Log.e("JSONUtils", " data = " + data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<Movie> parseJSON(String jsonData) {
        return null;
    }
}
