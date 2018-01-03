package com.example.android.booklisting;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by Yousef on 10/12/2017.
 */

public class ContainerObject {
    private String title, author, date;
    private Bitmap image;

    public ContainerObject(Bitmap inImage, String inName, String inAuthor, String inDate) {
        image = inImage;
        title = inName;
        author = inAuthor;
        date = inDate;
    }

    public Bitmap getImage(){return image;}
    public String getTitle(){return title;}
    public String getAuthor(){return author;}
    public String getDate(){return date;}
}
