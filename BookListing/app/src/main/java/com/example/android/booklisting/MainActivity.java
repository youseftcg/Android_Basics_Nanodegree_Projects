package com.example.android.booklisting;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<ContainerObject>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    cArrayAdapter arrayAdapter;
    String url;
    EditText searchBox;
    static ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.loading);

        searchBox = (EditText) findViewById(R.id.search_box);
        url = "https://www.googleapis.com/books/v1/volumes?q=intitle:&langRestrict=en&prettyPrint=false";


        Button searchButton = (Button) findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textFromSearch = searchBox.getText().toString();

                url = "https://www.googleapis.com/books/v1/volumes?q=intitle:"+textFromSearch+"&langRestrict=en&prettyPrint=false";
                getSupportLoaderManager().restartLoader(1,null,MainActivity.this);

            }
        });

        ListView listView = (ListView) findViewById(R.id.list);

        arrayAdapter = new cArrayAdapter(MainActivity.this, new ArrayList<ContainerObject>());

        listView.setAdapter(arrayAdapter);
        getSupportLoaderManager().initLoader(1, null, this);
        // Need to make an ArrayAdatpter and adapt a list view and to read more about loader
    }

    @Override
    public Loader<ArrayList<ContainerObject>> onCreateLoader(int id, Bundle args) {

        Log.e(LOG_TAG, "I'm in onCreateLoader");
        return new BookLoader(MainActivity.this, url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ContainerObject>> loader, ArrayList<ContainerObject> data) {
        Log.e(LOG_TAG, "I'm in onLoadFinished");
        progressBar.setVisibility(View.GONE);
        // Data gives null
        if (data != null) {
            arrayAdapter.clear();
            arrayAdapter.addAll(data);
            Log.e(LOG_TAG, "After if in onLoadFinished is it : " + arrayAdapter);

        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ContainerObject>> loader) {
        Log.e(LOG_TAG, "I'm in oneLoaderReset");

        arrayAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }
}
