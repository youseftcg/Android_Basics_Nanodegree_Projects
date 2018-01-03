package com.example.android.booklisting;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static android.R.attr.author;
import static com.example.android.booklisting.MainActivity.LOG_TAG;

/**
 * Created by Yousef on 10/16/2017.
 */

public class cArrayAdapter extends ArrayAdapter<ContainerObject> {
    public cArrayAdapter(Context context, ArrayList<ContainerObject> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        // if not , then create a view and save it to @convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.container, parent,false);
        }

        //Find all the views that I want to add data to them
        ImageView imageView = convertView.findViewById(R.id.book_image);
        TextView  titleView = convertView.findViewById(R.id.book_name);
        TextView authorView = convertView.findViewById(R.id.book_author);
        TextView datePublishedView = convertView.findViewById(R.id.date_published);

        // Get the current view of the list
        ContainerObject view = getItem(position);

        // Get All item's data
        Bitmap image = view.getImage();
        String title = view.getTitle();
        String author = view.getAuthor();
        String date = view.getDate();

        Log.e(LOG_TAG,"Data in arrayAdapter :  " + title + author + date);

            imageView.setImageBitmap(image);
            titleView.setText(title);
            authorView.setText(author);
            datePublishedView.setText(date);


//        if(imageView == null){imageView.setVisibility(View.INVISIBLE);}
//        else{}
//        if(titleView == null){titleView.setVisibility(View.INVISIBLE);}
//        else{
//            titleView.setText(title);
//        }
//        if(authorView == null){authorView.setVisibility(View.INVISIBLE);}
//        else{
//            authorView.setText(author);
//        }
//        if(datePublishedView == null){datePublishedView.setVisibility(View.INVISIBLE);}
//        else{
//            datePublishedView.setText(date);
//        }


    return convertView;
    }
}
