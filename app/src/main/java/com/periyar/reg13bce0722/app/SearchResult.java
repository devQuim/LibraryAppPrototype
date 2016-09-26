package com.periyar.reg13bce0722.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shubham on 01/01/2016.
 */
public class SearchResult extends ActionBarActivity {
    public static Toolbar toolbar;
    private ListView listView;
    public GetAlImage4 getAlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);
        toolbar = (Toolbar) findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listView = (ListView) findViewById(R.id.listBook);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else {
            showGPSDisabledAlertToUser();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Search Results");
        getURLs();
    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                                finish();

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void getURLs() {
        class GetURLs extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchResult.this, "Searching for Book.......", "Please Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s == null) {
                    Toast.makeText(getBaseContext(), "Network Error!", Toast.LENGTH_LONG).show();
                } else {
                    getAlImage = new GetAlImage4(s);
                    try {
                        getAlImage.getAllImage();
                        if (GetAlImage4.len == 0) {
                            alertUser();
                        }
                        toolbar.setSubtitle("Total Search Results : " + GetAlImage4.len);
                        toolbar.setSubtitleTextColor(getResources().getColor(R.color.ColorPrimaryDark));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CustomList5 customList = new CustomList5(SearchResult.this, GetAlImage4.title, GetAlImage4.author, GetAlImage4.publisher, GetAlImage4.edition, GetAlImage4.copies, GetAlImage4.loc);
                    listView.setAdapter(customList);
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {

                    URL url = new URL("http://xyz.php?Stitle="+MyLib.Stitle+"&Sauthor="+MyLib.Sauthor+"&Spublisher="+MyLib.Spublisher); //Your server side script's url address
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


                } catch (Exception e) {
                    return null;
                }
            }

        }
        GetURLs gu = new GetURLs();
        gu.execute();


    }
    private void alertUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("No Result Found !");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setMessage("Sorry, Book you are looking for is not available in our Library! \n" +
                "\n" +
                "Do you want to Recommend us?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                  startActivity(new Intent(SearchResult.this,RecommTab.class));
finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
