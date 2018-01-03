package com.example.android.booklisting;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static com.example.android.booklisting.MainActivity.LOG_TAG;
import static com.example.android.booklisting.MainActivity.progressBar;

/**
 * Created by Yousef on 10/16/2017.
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<ContainerObject>> {
    String input;
    public BookLoader(Context context, String link){
        super(context);
        Log.e(LOG_TAG,"I'm in BookLoader");
        input = link;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        ProgressBar loading = MainActivity.progressBar;
        loading.setVisibility(View.VISIBLE);
        Log.e(LOG_TAG,"I'm in onStartLoading before ");

        forceLoad();
        Log.e(LOG_TAG,"I'm in onStartLoading after");

    }

    @Override
    public ArrayList<ContainerObject> loadInBackground() {
        Log.e(LOG_TAG,"I'm in loadInBackground");

        return FetchingData.getData(input);
    }
}
