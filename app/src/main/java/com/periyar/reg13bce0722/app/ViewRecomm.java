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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shubham on 13/12/2015.
 */
public class ViewRecomm extends ActionBarActivity{
    private Toolbar toolbar;
  public int ii;
TextView titled,authord,isbnd,publisherd,copiesd,remarksd,rated,statusd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommdesc);
        toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

        }else{
            showGPSDisabledAlertToUser();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
        int i = intent.getIntExtra(ImageListView.BITMAP_ID,0);
        ii=i;

        getSupportActionBar().setTitle("Request ID: "+GetAlImage3.id[i]);
 titled=(TextView)findViewById(R.id.Titled);
        authord=(TextView)findViewById(R.id.Authord);
        isbnd=(TextView)findViewById(R.id.ISBNd);
        publisherd=(TextView)findViewById(R.id.Publisherd);
        copiesd=(TextView)findViewById(R.id.Copiesd);
        remarksd=(TextView)findViewById(R.id.Remarksd);
        rated=(TextView)findViewById(R.id.Rated);
        statusd=(TextView)findViewById(R.id.Statusd);
        titled.setText(GetAlImage3.title[i]);
       authord.setText(GetAlImage3.author[i]);
        if(GetAlImage3.isbn[i].toString().trim().equals("0")){
            isbnd.setText("N/A");
        }else {
            isbnd.setText(GetAlImage3.isbn[i]);
        }
        publisherd.setText(GetAlImage3.publisher[i]);
        copiesd.setText(GetAlImage3.copies[i]);
        remarksd.setText(GetAlImage3.remarks[i]);
        if(GetAlImage3.rate[i].toString().trim().equals("0")){
            rated.setText("N/A");
        }else {
            rated.setText(GetAlImage3.rate[i]);
        }
        statusd.setText(GetAlImage3.status[i]);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deleteit, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.deleteit:
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                   confirmDeleteRecomm();
                }else{
                    Toast.makeText(ViewRecomm.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                }
                break;

        }

        return false;
    }

    private void deleteRecomm(){
        class DeleteRecomm extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewRecomm.this, "Deleting...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s==null){
                    Toast.makeText(getBaseContext(), "Network Error!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ViewRecomm.this, s, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ViewRecomm.this, RecommTab.class));
                    finish();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                String s =sendGetRequestParam("http://xyz.php?ID=", GetAlImage3.id[ii]);
                return s;
            }
        }

        DeleteRecomm de = new DeleteRecomm();
        de.execute();
    }

    private void confirmDeleteRecomm(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this suggestion?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteRecomm();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public String sendGetRequestParam(String requestURL, String id){
        StringBuilder sb =new StringBuilder();
        try {
            URL url = new URL(requestURL+id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while((s=bufferedReader.readLine())!=null){
                sb.append(s+"\n");
            }
        }catch(Exception e){
        }
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewRecomm.this,RecommTab.class));
    }
}
