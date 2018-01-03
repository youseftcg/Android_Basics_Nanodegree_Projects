package com.example.android.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.R.attr.key;
import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsObject>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    //String url = "http://api.nytimes.com/svc/topstories/v2/technology.json?limit=10&api-key=f3ac6e6f76be4f63aa983e9b13e0bb1f";
    String url = "http://api.nytimes.com/svc/topstories/v2";
    NewsArrayAdapter mAdapter = null;
    TextView emptyView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting the ProgressBar to use it then in disabling it
        progressBar = (ProgressBar) findViewById(R.id.loading);


        final ListView listView = (ListView) findViewById(R.id.list);

        mAdapter = new NewsArrayAdapter(MainActivity.this, new ArrayList<NewsObject>());

        listView.setAdapter(mAdapter);

        Log.e(LOG_TAG, "I'm before initLoader");

        emptyView = (TextView) findViewById(R.id.no_data);

        if (isOnline()) {
            getSupportLoaderManager().initLoader(0, null, this);

            Log.e(LOG_TAG, "I'm after initLoader");

            // What to display when there're no data
            // to be displayed:
            listView.setEmptyView(emptyView);
            if (listView.getAdapter().getCount() < 1) {
                emptyView.setText("No data available.");
            }
        } else {
            progressBar.setVisibility(View.GONE);
            listView.setEmptyView(emptyView);
            emptyView.setText(R.string.no_internet);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsObject object = (NewsObject) mAdapter.getItem(i);
                String url = object.getUrl();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    // Checking whether the user is connected to INTERNET or not
    public boolean isOnline() {
        boolean result;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<ArrayList<NewsObject>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String category = sharedPreferences.getString(
                getString(R.string.category_key) ,
                getString(R.string.default_value));
        //UriBuilder to add queries
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(category+".json");
        uriBuilder.appendQueryParameter("api-key", "f3ac6e6f76be4f63aa983e9b13e0bb1f");

        return new NewsLoader(MainActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsObject>> loader, ArrayList<NewsObject> data) {
        progressBar.setVisibility(View.GONE);
        mAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsObject>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item){
            Intent intent = new Intent(this , SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
