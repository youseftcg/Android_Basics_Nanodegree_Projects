package com.example.android.news;

import android.graphics.Bitmap;

/**
 * Created by Yousef on 10/27/2017.
 */

public class NewsObject {
    private String title, date, url;
    private Bitmap image;

    public NewsObject(Bitmap inImage, String inDate, String inTitle, String inUrl) {
        image = inImage;
        date = inDate;
        title = inTitle;
        url = inUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
    public String getUrl(){
        return url;
    }
}
