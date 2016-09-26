package com.periyar.reg13bce0722.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shubham on 29/12/2015.
 */
public class GetAlImage3 {
    public static String[] id,title,author,isbn,publisher,copies,remarks,rate,status,recommby;
    //  public static Bitmap[] bitmaps;
    public static int len;
    public static final String JSON_ARRAY="result";

    private String json;
    //  public static int[] x;
    private JSONArray list;


    public GetAlImage3(String json){
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

        id = new String[list.length()];
        title = new String[list.length()];
        author = new String[list.length()];
        isbn = new String[list.length()];
        publisher=new String[list.length()];
        copies = new String[list.length()];
        remarks = new String[list.length()];
        rate= new String[list.length()];
        status = new String[list.length()];
        recommby= new String[list.length()];

        for(int i=0;i<list.length();i++){
            id[i] = list.getJSONObject(i).getString("ID");
            recommby[i] = list.getJSONObject(i).getString("RecommBy");

            title[i] = list.getJSONObject(i).getString("Title");
           author[i]=list.getJSONObject(i).getString("Author");
            isbn[i] = list.getJSONObject(i).getString("ISBN");
            publisher[i] = list.getJSONObject(i).getString("Publisher");
            copies[i] = list.getJSONObject(i).getString("Copies");
            remarks[i]=list.getJSONObject(i).getString("Remarks");
           rate[i] = list.getJSONObject(i).getString("Rate");
            status[i] = list.getJSONObject(i).getString("Status");

            // JSONObject jsonObject = list.getJSONObject(i);
            //     bitmaps[i]=getImage(jsonObject);
        }
    }
}
