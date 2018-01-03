package com.example.android.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp.Data.ProductContract.ProductEntry;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ProductsCursorAdapter productsCursorAdapter;
    SQLiteDatabase db;
public static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intentAdd);
            }
        });



        ListView listView = (ListView) findViewById(R.id.list);

        productsCursorAdapter = new ProductsCursorAdapter(this, null);

        listView.setAdapter(productsCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intentUpdate = new Intent(MainActivity.this, EditorActivity.class);
                Uri contentUriWithId = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                intentUpdate.setData(contentUriWithId);
                startActivity(intentUpdate);
            }
        });
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Add a button for adding a dummy data
        switch (item.getItemId()) {
            case R.id.insert_dummy:
                ContentValues contentValues = new ContentValues();
                contentValues.put(ProductEntry.COLUMN_NAME, "Test");
                contentValues.put(ProductEntry.COLUMN_QUANTITY, 5);
                contentValues.put(ProductEntry.COLUMN_PRICE, 70);
                getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
                return true;
            case R.id.delete_all:
                getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e("MainActivity.this", "I'm in OnCreate");
        String[] projection = {
                ProductEntry.COLUMN_ID,
                ProductEntry.COLUMN_PIC,
                ProductEntry.COLUMN_NAME,
                ProductEntry.COLUMN_QUANTITY,
                ProductEntry.COLUMN_PRICE
        };
        Log.e("MainActivity.this", "I'm at the end of OnCreate");

        return new CursorLoader(this, ProductEntry.CONTENT_URI, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e("MainActivity.this", "Cursor data = " + data);
            productsCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productsCursorAdapter.swapCursor(null);
    }

}
