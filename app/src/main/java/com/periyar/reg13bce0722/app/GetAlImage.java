package com.periyar.reg13bce0722.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by Shubham on 13/12/2015.
 */
public class GetAlImage {

    public static String[] imageURLs,idItem,itemItem,detailItem,datee;
  //  public static Bitmap[] bitmaps;
   public static int len;
    public static final String JSON_ARRAY="result";
    public static final String IMAGE_URL = "Photo";
    private String json;
    private JSONArray list;


    public GetAlImage(String json){
        this.json = json;
        try {
            JSONObject jsonObject = new JSONObject(this.json);
            list= jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/*
    private Bitmap getImage(JSONObject jo){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(jo.getString(IMAGE_URL));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }
*/
    public void getAllImage() throws JSONException {
        len=list.length();
        lafCache.cache=true;
      //  bitmaps = new Bitmap[list.length()];

        imageURLs = new String[list.length()];
        idItem = new String[list.length()];
        itemItem = new String[list.length()];
        detailItem = new String[list.length()];
datee=new String[list.length()];
        for(int i=0;i<list.length();i++){
            imageURLs[i] = list.getJSONObject(i).getString(IMAGE_URL);
            idItem[i] = list.getJSONObject(i).getString("ID");
            datee[i]=list.getJSONObject(i).getString("datee");
            itemItem[i] = list.getJSONObject(i).getString("Item");
            detailItem[i] = list.getJSONObject(i).getString("Details");
            System.out.println("@@@@@@@@@@@@@@@@@@@@"+imageURLs[i]+"@@@@@@@@@@@@@@@@@@@@");
           // JSONObject jsonObject = list.getJSONObject(i);
       //     bitmaps[i]=getImage(jsonObject);
        }
    }
}