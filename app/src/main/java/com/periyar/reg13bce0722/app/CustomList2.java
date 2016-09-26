package com.periyar.reg13bce0722.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Shubham on 16/12/2015.
 */
public class CustomList2 extends ArrayAdapter<String> {
    private String[] titles, Subs;
    private int[] icons;
    private Activity context;


    public CustomList2(Activity context, String[] titles, int[] icons, String[] Subs) {
        super(context, R.layout.slideritem, titles);
        this.titles = titles;
        this.icons = icons;
        this.Subs = Subs;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.slideritem, null, true);
        ImageView icon = (ImageView) listViewItem.findViewById(R.id.menuicon);
        TextView item = (TextView) listViewItem.findViewById(R.id.menuitem);
        TextView sub = (TextView) listViewItem.findViewById(R.id.menusub);
        icon.setImageResource(icons[position]);
        item.setText(titles[position]);
        sub.setText(Subs[position]);
        return listViewItem;
    }
}