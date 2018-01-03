package com.example.android.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yousef on 10/28/2017.
 */

public class NewsArrayAdapter extends ArrayAdapter<NewsObject> {
    public NewsArrayAdapter(@NonNull Context context, @NonNull List<NewsObject> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_holder, parent, false);
        }

        //Getting holders
        ImageView photoHolder = convertView.findViewById(R.id.photo);
        TextView dateHolder = convertView.findViewById(R.id.date);
        TextView titleHolder = convertView.findViewById(R.id.title);

        NewsObject item = getItem(position);
        //Getting values
        Bitmap imageValue = item.getImage();
        String dateValue  =  item.getDate();
        String titleValue = item.getTitle();

        photoHolder.setImageBitmap(imageValue);
        dateHolder.setText(dateValue);
        titleHolder.setText(titleValue);
        return convertView;
    }
}
