package com.periyar.reg13bce0722.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shubham on 23/01/2016.
 */
public class CustomList6 extends ArrayAdapter<String> {
public static String[] date,title;

private Activity context;

public CustomList6(Activity context, String[] title,String[] date) {
        super(context, R.layout.issitem,title);
        this.date=date;
        this.title=title;
    this.context = context;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.issitem, null, true);


        TextView title1=(TextView)listViewItem.findViewById(R.id.isstit);
        TextView date1=(TextView)listViewItem.findViewById(R.id.issd);
       TextView leftday=(TextView)listViewItem.findViewById(R.id.issleft);

           title1.setText("Title      : "+GetAlImage5.title[position]);
        date1.setText("Issue Date : " + GetAlImage5.date[position]);


       /* SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
                java.util.Date cdate = format.parse(dtStart);
                java.util.Date ldate = format.parse(dtStart);

        } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }*/
       String dtStart = GetAlImage5.date[position];
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");

        try {

                Date oldDate = dateFormat.parse(dtStart);
                System.out.println(oldDate);

                Date currentDate = new Date();

                long diff = currentDate.getTime() - oldDate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;

                if (days>14) {
                    leftday.setText("Late By : " +(days-14) +" day(s)");
                    lafCache.penalty= (int) (days-14);
                }
else if(days<14){
                    leftday.setText("Days Left : " +(14-days) );
                }
            else if(days==14){
                    leftday.setText("Renew/Return Today!");
                    if(lafCache.penalty==-1) {
                        lafCache.penalty = 0;
                    }
                }


        } catch (ParseException e) {

                e.printStackTrace();
        }


        // Photo.setImageBitmap(bitmaps[position]);
        return  listViewItem;
        }


        }
