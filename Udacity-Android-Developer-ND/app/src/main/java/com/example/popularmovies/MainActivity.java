package com.example.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MovieAsyncTask().execute();
    }

    private class MovieAsyncTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            URL url = JSONUtils.getUrl(JSONUtils.ORDER_BY_POPULAR);
            String data = JSONUtils.getData(url);

            Log.e("MainActivity", "DATA = " + data);

            return data;
        }
    }

}
