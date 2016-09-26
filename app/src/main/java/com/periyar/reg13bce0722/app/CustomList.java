package com.periyar.reg13bce0722.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Shubham on 13/12/2015.
 */
public class CustomList extends ArrayAdapter<String> {
    private String[] urls,idItem,itemItem,detailItem,datee;
    private Bitmap[] bitmaps;
    private Activity context;


    public CustomList(Activity context, String[] urls, String[] datee,String[] idItem,String[] itemItem,String[] detailItem) {
        super(context, R.layout.lafitem, urls);
        this.idItem=idItem;
        this.itemItem=itemItem;
        this.detailItem=detailItem;
        this.context = context;
        this.urls= urls;
        this.datee=datee;
     //   this.bitmaps= bitmaps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.lafitem, null, true);
        TextView ID = (TextView) listViewItem.findViewById(R.id.iddd);
        TextView Item = (TextView) listViewItem.findViewById(R.id.item);
       // ImageView Photo = (ImageView) listViewItem.findViewById(R.id.lafimg);
        TextView dat=(TextView)listViewItem.findViewById(R.id.datee);
        ID.setText(idItem[position]);
        Item.setText(itemItem[position]);
        dat.setText(datee[position]);
       // Photo.setImageBitmap(bitmaps[position]);
        return  listViewItem;
    }
}