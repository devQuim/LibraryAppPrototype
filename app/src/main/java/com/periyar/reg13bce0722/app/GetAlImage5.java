package com.periyar.reg13bce0722.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shubham on 23/01/2016.
 */
public class GetAlImage5 {
    public static String[] title,recommby,date;

    public static int len;
    public static final String JSON_ARRAY="result";

    private String json;
    //  public static int[] x;
    private JSONArray list;


    public GetAlImage5(String json){
        this.json = json;
        try {
            JSONObject jsonObject = new JSONObject(this.json);
            list= jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAllImage() throws JSONException {
        len=list.length();
        //  bitmaps = new Bitmap[list.length()];

        recommby = new String[list.length()];
        title = new String[list.length()];
        date = new String[list.length()];
        for(int i=0;i<list.length();i++){
            recommby[i] = list.getJSONObject(i).getString("RecommBy");
            title[i] = list.getJSONObject(i).getString("Title");
            date[i]=list.getJSONObject(i).getString("Date");

        }
    }
}
