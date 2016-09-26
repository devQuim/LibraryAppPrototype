package com.periyar.reg13bce0722.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Shubham on 01/01/2016.
 */
public class CustomList5 extends ArrayAdapter<String> {
    public static String[] title,author,publisher,edition,copies,loc;

    private Activity context;
    //  private int[] x;



    public CustomList5(Activity context,String[] title,String[] author,String[] publisher,String[] edition,String[] copies,String[] loc) {
        super(context, R.layout.searchitem, title);

        this.title=title;
        this.author=author;
        this.context = context;
        this.edition= edition;
        this.copies= copies;
        this.publisher=publisher;
        this.loc=loc;
        // x=num;
        //   this.bitmaps= bitmaps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.searchitem, null, true);


        TextView title1=(TextView)listViewItem.findViewById(R.id.resTitle);
        TextView author1=(TextView)listViewItem.findViewById(R.id.resAuthor);
        TextView edition1=(TextView)listViewItem.findViewById(R.id.resEdition);
        TextView publisher1 = (TextView) listViewItem.findViewById(R.id.resPublisher);
        TextView copies1 = (TextView) listViewItem.findViewById(R.id.resCopies);
        TextView loc1 = (TextView) listViewItem.findViewById(R.id.resLoc);


        title1.setText(GetAlImage4.title[position]);
        author1.setText(GetAlImage4.author[position]);
        publisher1.setText(GetAlImage4.publisher[position]);
        edition1.setText(GetAlImage4.edition[position]);
      copies1.setText(GetAlImage4.copies[position]);
        loc1.setText(GetAlImage4.loc[position]);
        return  listViewItem;
    }

}