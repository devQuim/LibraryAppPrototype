package com.periyar.reg13bce0722.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Shubham on 31/12/2015.
 */
public class MyLib extends ActionBarActivity {
    public static Toolbar toolbar;

    public  EditText serTitle,serAuthor,serPublisher;
    public static String Stitle,Sauthor,Spublisher;
    public Button serBut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylib);
        toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.ColorPrimaryDark));
        toolbar.setSubtitle("Search Book");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

        }else{
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
        getSupportActionBar().setTitle("My Library");

serTitle=(EditText)findViewById(R.id.serTitle);
        serAuthor=(EditText)findViewById(R.id.serAuthor);

        serPublisher=(EditText)findViewById(R.id.serPublisher);
        serBut=(Button)findViewById(R.id.submitSearch);

        serBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(lafCache.regid.equals("Reg. No.")){
                    Toast.makeText(MyLib.this, "Login First !", Toast.LENGTH_SHORT).show();
                }
                else */if(serTitle.getText().toString().trim().equals("")&&serAuthor.getText().toString().trim().equals("")&&serPublisher.getText().toString().trim().equals("")){
                    Toast.makeText(MyLib.this, "Please Fill At Least One Fields!", Toast.LENGTH_SHORT).show();
                }

                else {
                    Stitle = serTitle.getText().toString().trim().toUpperCase().replace("'", "\\'");
                    Sauthor = serAuthor.getText().toString().trim().toUpperCase().replace("'", "\\'");
                    Spublisher = serPublisher.getText().toString().trim().toUpperCase().replace("'","\\'");
                    startActivity(new Intent(MyLib.this,SearchResult.class));

                }
            }
        });
    }




    private void showGPSDisabledAlertToUser(){
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



}
