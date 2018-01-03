package com.example.android.inventoryapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.inventoryapp.Data.ProductContract.ProductEntry;


public class ProductsDbHelper extends SQLiteOpenHelper {

    //For constructors
    public static final String DATABASE_NAME = "product.db";
    private static final int DATABASE_VERSION = 2;
    private static final String COMMA = " , ";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";


    private static final String CREATE_TABLE =
                    "CREATE TABLE IF NOT EXISTS " + ProductEntry.TABLE_NAME + " (" +
                    ProductEntry.COLUMN_ID + INTEGER_TYPE + " PRIMARY KEY" +COMMA +
                    ProductEntry.COLUMN_PIC + TEXT_TYPE + COMMA +
                    ProductEntry.COLUMN_NAME + TEXT_TYPE + " NOT NULL" + COMMA +
                    ProductEntry.COLUMN_QUANTITY + INTEGER_TYPE + " DEFAULT 0" + COMMA +
                    ProductEntry.COLUMN_PRICE + " REAL" + " DEFAULT 0" + ")" + ";";

    public ProductsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ProductEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
