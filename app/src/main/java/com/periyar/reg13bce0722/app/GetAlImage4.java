package com.periyar.reg13bce0722.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shubham on 01/01/2016.
 */
public class GetAlImage4 {
    public static String[] title,author,publisher,copies,loc,edition;
    public static int len;
    public static final String JSON_ARRAY="result";

    private String json;
    //  public static int[] x;
    private JSONArray list;


    public GetAlImage4(String json){
        this.json = json;
        try {
            JSONObject jsonObject = new JSONObject(this.json);
            list= jsonObject.getJSONArray(JSON_ARRAY);
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$    "+list+"    $$$$$$$$$$$$$$$$$$$$$$$$$$");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAllImage() throws JSONException {
        len=list.length();

        title = new String[list.length()];
        author = new String[list.length()];
        edition = new String[list.length()];
        loc = new String[list.length()];
        copies = new String[list.length()];
        publisher=new String[list.length()];

        for(int i=0;i<list.length();i++){
            title[i] = list.getJSONObject(i).getString("serTitle");
            author[i]=list.getJSONObject(i).getString("serAuthor");
            edition[i] = list.getJSONObject(i).getString("serEdition");
            publisher[i] = list.getJSONObject(i).getString("serPublisher");
            loc[i] = list.getJSONObject(i).getString("serLoc");
           copies[i] = list.getJSONObject(i).getString("serCopies");

        }
    }
}
