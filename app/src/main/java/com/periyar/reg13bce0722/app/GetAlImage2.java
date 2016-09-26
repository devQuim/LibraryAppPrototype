package com.periyar.reg13bce0722.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shubham on 13/12/2015.
 */
public class GetAlImage2 {

    public static String[] imageURLs,id,head,foot,desc;
    //  public static Bitmap[] bitmaps;
    public static int len;
    public static final String JSON_ARRAY="result";
    public static final String IMAGE_URL = "Banner";
    private String json;
  //  public static int[] x;
    private JSONArray list;


    public GetAlImage2(String json){
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
        //  bitmaps = new Bitmap[list.length()];

        imageURLs = new String[list.length()];
        id = new String[list.length()];
       // x=new int[list.length()];
        head = new String[list.length()];
        foot = new String[list.length()];
        desc=new String[list.length()];
       /* for(int j=0;j<list.length();j++){
            Random back = new Random();
        //    x[j] = back.nextInt(8);
        }*/

        for(int i=0;i<list.length();i++){
            imageURLs[i] = list.getJSONObject(i).getString(IMAGE_URL);
            id[i] = list.getJSONObject(i).getString("ID");
            head[i]=list.getJSONObject(i).getString("Heading");
            foot[i] = list.getJSONObject(i).getString("Shot");
            desc[i] = list.getJSONObject(i).getString("Description");
            System.out.println("@@@@@@@@@@@@@@@@@@@@"+imageURLs[i]+"@@@@@@@@@@@@@@@@@@@@");
            // JSONObject jsonObject = list.getJSONObject(i);
            //     bitmaps[i]=getImage(jsonObject);
        }
    }
}