package com.periyar.reg13bce0722.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Shubham on 13/12/2015.
 */
public class CustomList4 extends ArrayAdapter<String> {
    public static String[] id,title,author,isbn,publisher,copies,remarks,rate,status;

    private Activity context;
    //  private int[] x;



    public CustomList4(Activity context, String[] id,String[] title,String[] author,String[] isbn,String[] publisher, String[] copies,String[] remarks,String[] rate,String[] status) {
        super(context, R.layout.recommitem, id);
        this.id=id;
        this.title=title;
        this.author=author;
        this.context = context;
        this.isbn= isbn;
        this.publisher=publisher;

        this.copies=copies;
        this.remarks=remarks;
        this.rate = rate;
        this.status= status;

        // x=num;
        //   this.bitmaps= bitmaps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.recommitem, null, true);

        TextView id1 = (TextView) listViewItem.findViewById(R.id.reqid);
        TextView title1=(TextView)listViewItem.findViewById(R.id.titleofbook);
        TextView status1=(TextView)listViewItem.findViewById(R.id.statusofbook);
        TextView author1=(TextView)listViewItem.findViewById(R.id.authorofbook);


        id1.setText("R.ID : "+GetAlImage3.id[position]);
        title1.setText("Title : "+GetAlImage3.title[position]);
        author1.setText("Author : " + GetAlImage3.author[position]);
        status1.setText("Status : " + GetAlImage3.status[position]);

        // Photo.setImageBitmap(bitmaps[position]);
        return  listViewItem;
    }

}