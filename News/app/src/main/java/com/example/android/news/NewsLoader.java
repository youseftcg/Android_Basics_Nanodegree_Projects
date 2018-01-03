package com.example.android.news;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;

import static android.R.attr.data;
import static com.example.android.news.Fetching.fetchData;
import static com.example.android.news.MainActivity.LOG_TAG;

/**
 * Created by Yousef on 10/28/2017.
 */

public class NewsLoader extends AsyncTaskLoader<ArrayList<NewsObject>> {
    String linkProvided = null;


    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG, "I'm onStartLoading");

        super.onStartLoading();
        forceLoad();
    }
    public NewsLoader(Context context, String link) {

        super(context);
        Log.e(LOG_TAG, "I'm in Constructor");
        linkProvided = link;
    }

    @Override
    public ArrayList<NewsObject> loadInBackground() {
        Log.e(LOG_TAG, "I'm in loadInBackground");
        if(linkProvided == null){
            return null;}
        Log.e(LOG_TAG, "I'm after if loadInBackground, data isn't null");
        ArrayList<NewsObject> data = Fetching.fetchData(linkProvided);

        return data;
    }

}
