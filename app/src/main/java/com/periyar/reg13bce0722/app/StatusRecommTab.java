package com.periyar.reg13bce0722.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shubham on 28/12/2015.
 */
public class StatusRecommTab extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    public GetAlImage3 getAlImage;
    public static final String BITMAP_ID = "BITMAP_ID";
    public static final String GET_IMAGE_URL="http://myperiyar.esy.es/RecommRet.php?Reg_id=";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.statusrecommtab,container,false);
        listView = (ListView) v.findViewById(R.id.listRecomm);
        listView.setOnItemClickListener(this);
        getURLs();
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ViewRecomm.class);
        intent.putExtra(BITMAP_ID, position);
        startActivity(intent);
        getActivity().finish();
    }

    public  void getURLs() {
     class GetURLs extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Retrieving Data.......","Please Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s==null){
                    Toast.makeText(getView().getContext(), "Network Error!", Toast.LENGTH_LONG).show();
                }else {
                    getAlImage = new GetAlImage3(s);
                    try {
                        getAlImage.getAllImage();


                        RecommTab.toolbar.setSubtitle("Books Suggested : " + GetAlImage3.len);
                        RecommTab.toolbar.setSubtitleTextColor(getResources().getColor(R.color.ColorPrimaryDark));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CustomList4 customList = new CustomList4(getActivity(), GetAlImage3.id, GetAlImage3.title, GetAlImage3.author, GetAlImage3.isbn, GetAlImage3.publisher, GetAlImage3.copies, GetAlImage3.remarks, GetAlImage3.rate, GetAlImage3.status);
                    listView.setAdapter(customList);
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    con.setConnectTimeout(300000);
                    con.setReadTimeout(30000);
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }

        }
        GetURLs gu = new GetURLs();
        gu.execute(GET_IMAGE_URL + lafCache.regid);




    }

}
