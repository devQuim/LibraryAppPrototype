package com.periyar.reg13bce0722.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by Shubham on 13/12/2015.
 */
public class ImageListView extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private Toolbar toolbar;
    public static final String GET_IMAGE_URL="http://xyz.php";  //Your server side script's url address

    public GetAlImage getAlImage;

    public static final String BITMAP_ID = "BITMAP_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostnfoundcontainer);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

        }else{
            showGPSDisabledAlertToUser();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setSubtitleTextColor(R.color.subtitle);
        toolbar.setSubtitle("Found Items");


        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Lost & Found");

        listView = (ListView) findViewById(R.id.lafEntry);
        listView.setOnItemClickListener(this);
        getURLs();
    }

/*

    private void getImages(){
        class GetImages extends AsyncTask<Void,Void,Void>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ImageListView.this,"Downloading Lost & Found Data...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                loading.dismiss();

                CustomList customList = new CustomList(ImageListView.this,GetAlImage.imageURLs,GetAlImage.datee,GetAlImage.idItem,GetAlImage.itemItem,GetAlImage.detailItem);
                listView.setAdapter(customList);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getAlImage.getAllImage();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
    }
*/
    private void getURLs() {
        class GetURLs extends AsyncTask<String,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ImageListView.this,"Retrieving Lost & Found Data...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s==null){
                    Toast.makeText(getBaseContext(), "Network Error!", Toast.LENGTH_LONG).show();
                }else {
                    getAlImage = new GetAlImage(s);
                    try {
                        getAlImage.getAllImage();
                        if (GetAlImage.len == 0) {
                            LinearLayout x = (LinearLayout) findViewById(R.id.nolaf);
                            x.setBackgroundResource(R.drawable.nolaf);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CustomList customList = new CustomList(ImageListView.this, GetAlImage.imageURLs, GetAlImage.datee, GetAlImage.idItem, GetAlImage.itemItem, GetAlImage.detailItem);
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
        gu.execute(GET_IMAGE_URL);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       Intent intent = new Intent(this, ViewDetail.class);
        intent.putExtra(BITMAP_ID, i);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        lafCache.img=GetAlImage.imageURLs;
        lafCache.idd=GetAlImage.idItem;
        lafCache.item=GetAlImage.itemItem;
       lafCache.detail=GetAlImage.detailItem;
        lafCache.datee=GetAlImage.datee;

        finish();
    }
  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    getURLs();
                } else {
                    Toast.makeText(ImageListView.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                }
                break;

        }

        return false;
    }
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                                finish();

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
