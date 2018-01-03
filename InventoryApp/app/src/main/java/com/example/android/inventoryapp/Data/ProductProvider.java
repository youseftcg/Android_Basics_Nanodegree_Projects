package com.example.android.inventoryapp.Data;

        import android.content.ContentProvider;
        import android.content.ContentUris;
        import android.content.ContentValues;
        import android.content.Context;
        import android.content.UriMatcher;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.net.Uri;

        import com.example.android.inventoryapp.Data.ProductContract.ProductEntry;

public class ProductProvider extends ContentProvider {

    public static final int PRODUCT = 100;
    public static final int PRODUCT_ID = 101;
    public static ProductsDbHelper productsDbHelper;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCT);
        mUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        productsDbHelper = new ProductsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String setOrder) {
        //Getting data that will be reacted upon
        SQLiteDatabase database = productsDbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                cursor = database.query(
                        ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, setOrder);
                break;
            case PRODUCT_ID:
                selection = ProductEntry.COLUMN_ID + "=?";
                String id = String.valueOf(ContentUris.parseId(uri));
                selectionArgs = new String[]{id};
                cursor = database.query(
                        ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, setOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        //Getting data that will be reacted upon
        SQLiteDatabase database = productsDbHelper.getWritableDatabase();
        database.insert(ProductEntry.TABLE_NAME, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        //Getting data that will be reacted upon
        SQLiteDatabase database = productsDbHelper.getWritableDatabase();

        int updatedRows = 0;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                updatedRows = database.update(ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductEntry.COLUMN_ID + "=?";
                String id = String.valueOf(ContentUris.parseId(uri));
                selectionArgs = new String[]{id};

                updatedRows = database.update(ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        if (updatedRows != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Getting data that will be reacted upon
        SQLiteDatabase database = productsDbHelper.getWritableDatabase();
        int deletedRows = 0;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                deletedRows = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductEntry.COLUMN_ID + "=?";
                String id = String.valueOf(ContentUris.parseId(uri));
                selectionArgs = new String[]{id};
                deletedRows = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri : " + uri);
        }
    }
}

