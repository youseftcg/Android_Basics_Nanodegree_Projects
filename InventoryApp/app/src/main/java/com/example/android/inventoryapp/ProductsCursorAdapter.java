package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.inventoryapp.Data.ProductContract.ProductEntry;

import java.io.ByteArrayInputStream;

import static com.example.android.inventoryapp.MainActivity.LOG_TAG;


public class ProductsCursorAdapter extends CursorAdapter {


    public ProductsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_view, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Getting the viewHolders.
        ImageView picSummary = view.findViewById(R.id.imageView);
        TextView nameSummary = view.findViewById(R.id.name_summary);
        TextView quantitySummary = view.findViewById(R.id.quantity_summary);
        TextView priceSummary = view.findViewById(R.id.price_summary);

        //Getting Columns indices (int).
        int picColoumnIndex = cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PIC);
        int nameColoumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_NAME);
        int quantityColoumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
        int priceColoumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);

        boolean isTherePic = cursor.isNull(picColoumnIndex);
        //Retrieving data from the DataBase.
        String photoPath = null;
        if(!isTherePic){
            photoPath = cursor.getString(picColoumnIndex);
//            byte[] dbPic = cursor.getBlob(picColoumnIndex);
//            pic = decodeByteImage(dbPic);
        }
        String name = cursor.getString(nameColoumnIndex);
        String quantity = String.valueOf(cursor.getInt(quantityColoumnIndex));
        String price = String.valueOf(cursor.getInt(priceColoumnIndex));

        //Setting the retrieved data into the ViewsHolders.
        if(photoPath != null && !TextUtils.isEmpty(photoPath)) {
        Uri uriPhoto = Uri.parse(photoPath);
            picSummary.setImageURI(uriPhoto);
            picSummary.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            picSummary.setImageBitmap(pic);
        }
        nameSummary.setText(name);
        quantitySummary.setText(quantity);
        priceSummary.setText(price);


    }

    public static Bitmap decodeByteImage(byte[] photoBytes){

//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(photoBytes);

       // BitmapFactory.decodeStream(byteArrayInputStream);
        return BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
    }
}
