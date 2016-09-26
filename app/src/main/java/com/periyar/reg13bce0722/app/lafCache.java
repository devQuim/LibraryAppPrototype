package com.periyar.reg13bce0722.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Created by Shubham on 14/12/2015.
 */
public class lafCache extends Activity {
    public static boolean cache = false;
    public static boolean proxy = false;
    public static boolean lob = false;
    public static String locate;
    public static String[] img;
    public static String[] idd;
    public static String[] item;
    public static String[] detail, datee;
    public static String[] CalledNo = new String[1000];
    public static int count = 0;
    public static Bitmap[] bit;
    public static boolean login = false;
    public static boolean smsfeed = false;
    public static String nameid = "Name";
    public static String regid = "Reg. No.";
    public static boolean firsttime = true;
    public static int mod;
    public static boolean status = false;
    public static int statCount = 1;
    public static int penalty = -1;
    static Bitmap profile;
    public static String roleid = "Undefined";

    public lafCache(String[] imageURLs, String[] dat, String[] idItem, String[] itemItem, String[] detailItem) {
        img = imageURLs;
        idd = idItem;
        item = itemItem;
        detail = detailItem;
        //bit=bitmaps;
        datee = dat;

        cache = true;


    }

    public lafCache() {
    }

    public lafCache(Context context) {
        Intent H = new Intent(context, Home.class);
        startActivity(H);
    }
}
