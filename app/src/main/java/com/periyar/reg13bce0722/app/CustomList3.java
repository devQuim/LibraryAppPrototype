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
public class CustomList3 extends ArrayAdapter<String> {
    private String[] urls,idEvent,headEvent,shotEvent,desc;
    private Bitmap[] bitmaps;
    private Activity context;
  //  private int[] x;



    public CustomList3(Activity context, String[] urls, String[] id,String[] head,String[] shot,String[] desc) {
        super(context, R.layout.eventitem, urls);
        this.idEvent=id;
        this.headEvent=head;
        this.shotEvent=shot;
        this.context = context;
        this.urls= urls;
        this.desc=desc;
       // x=num;
        //   this.bitmaps= bitmaps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.eventitem, null, true);

listViewItem.setBackgroundResource(swapListBack(position%5));

        TextView head = (TextView) listViewItem.findViewById(R.id.ehead);

       // x =(ListView) listViewItem.findViewById(R.id.eventEntry);

        TextView foot=(TextView)listViewItem.findViewById(R.id.efoot);

        head.setText(idEvent[position]+" "+headEvent[position]);

        foot.setText(shotEvent[position]);

        // Photo.setImageBitmap(bitmaps[position]);
        return  listViewItem;
    }

    private int swapListBack(int set) {

        if (set == 0) {

          return(R.drawable.bac0);

        } else if (set == 1) {


           return (R.drawable.bac1);
        } else if (set == 2) {

            return (R.drawable.bac2);
        } else if (set == 3) {
            return (R.drawable.bac3);

        } else if (set == 4) {

            return (R.drawable.bac4);
        }

        return R.drawable.bac0;
    }


}