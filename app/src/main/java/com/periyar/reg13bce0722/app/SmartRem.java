package com.periyar.reg13bce0722.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Shubham on 23/01/2016.
 */
public class SmartRem extends ActionBarActivity implements AdapterView.OnItemClickListener{


    public static Toolbar toolbar;
public EditText issbook;
    private ListView listView;
    public GetAlImage5 getAlImage;
    public static final String BITMAP_ID = "BITMAP_ID";
    public static final String GET_IMAGE_URL="http://myperiyar.esy.es/SmartRemRet.php?Reg_id=";
    public Button setiss;
    public TextView issdate;
    public Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartrem);



        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.toolbar7);
        setSupportActionBar(toolbar);
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
        getSupportActionBar().setTitle("Smart Reminder");

issbook=(EditText)findViewById(R.id.issbook);
        listView = (ListView) findViewById(R.id.listiss);
        listView.setOnItemClickListener(this);
        issdate=(TextView)findViewById(R.id.issdate);
        getURLs();

        issdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SmartRem.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
setiss=(Button)findViewById(R.id.setiss);
        setiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lafCache.regid.equals("Reg. No.")) {
                    Toast.makeText(getBaseContext(), "Login First !", Toast.LENGTH_SHORT).show();
                } else if (issbook.getText().toString().trim().equals("") || issdate.getText().toString().trim().equals("Select Date")) {
                    Toast.makeText(getBaseContext(), "Please Fill Up All Compulsory Fields!", Toast.LENGTH_SHORT).show();
                } else {
                    addIss();
                }
            }
        });
    }
    private void addIss() {

        final String issbook1  = issbook.getText().toString().trim().toUpperCase().replace("'", "\\'");
        final String date = issdate.getText().toString().trim();

        class AddIss extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SmartRem.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               loading.dismiss();
                if(s==null){
                    Toast.makeText(getBaseContext(), "Network Error!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                    getURLs();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("RecommBy",lafCache.regid);
                params.put("Title",issbook1);
                params.put("Date",date);
                String res = sendPostRequest("http://xyz.php", params);  //Your server side script's url address
                return res;
            }
        }

        AddIss ae = new AddIss();
        ae.execute();

    }


    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date currentDate = new Date();

        if(myCalendar.getTime().after(currentDate))
        {
            Toast.makeText(SmartRem.this, "Invalid Issuing Date!", Toast.LENGTH_LONG).show();
        }else{
            issdate.setText(sdf.format(myCalendar.getTime()));
        }
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
                if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

                  getURLs();
                }else{
                    Toast.makeText(SmartRem.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
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

    public String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams) {
        //Creating a URL
        URL url;

        //StringBuilder object to store the message retrieved from the server
        StringBuilder sb = new StringBuilder();
        try {
            //Initializing Url
            url = new URL(requestURL);

            //Creating an httmlurl connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Configuring connection properties
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //Creating an output stream
            OutputStream os = conn.getOutputStream();

            //Writing parameters to the request
            //We are using a method getPostDataString which is defined below
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                //Reading server response
                while ((response = br.readLine()) != null){
                    sb.append(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    public  void getURLs() {
        class GetURLs extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SmartRem.this,"Retrieving Data.......","Please Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s==null){
                    Toast.makeText(getBaseContext(), "Network Error!", Toast.LENGTH_LONG).show();
                }else {
                    getAlImage = new GetAlImage5(s);
                    try {
                        getAlImage.getAllImage();


                       SmartRem.toolbar.setSubtitle("Books Issued : " + GetAlImage5.len);
                        SmartRem.toolbar.setSubtitleTextColor(getResources().getColor(R.color.ColorPrimaryDark));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CustomList6 customList = new CustomList6(SmartRem.this, GetAlImage5.title, GetAlImage5.date);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            confirmDeleteRecomm(position);
        }else{
            Toast.makeText(SmartRem.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
        }
    }
    private void confirmDeleteRecomm(final int pos){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to remove this entry ? \n\n Title : "+GetAlImage5.title[pos]);

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteRecomm(pos);

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
    private void deleteRecomm(final int po){
        class DeleteRecomm extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SmartRem.this, "Removing...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s==null){
                    Toast.makeText(getBaseContext(), "Network Error!", Toast.LENGTH_LONG).show();
                }else {
                    lafCache.penalty=-1;
                    Toast.makeText(SmartRem.this, s, Toast.LENGTH_LONG).show();
                   getURLs();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                String s =sendGetRequestParam("http://xyz.php?Title=", GetAlImage5.title[po].toUpperCase().trim(),lafCache.regid);

                return s;
            }
        }

        DeleteRecomm de = new DeleteRecomm();
        de.execute();
    }
    public String sendGetRequestParam(String requestURL, String id,String reg){
        StringBuilder sb =new StringBuilder();
        try {
String x=requestURL+""+id+""+"&Reg_id="+reg;
            System.out.println("######--"+x+"--######");
            URL url = new URL(x);

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
}
