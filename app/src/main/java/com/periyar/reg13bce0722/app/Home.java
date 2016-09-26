package com.periyar.reg13bce0722.app;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Shubham on 16/12/2015.
 */
public class Home extends ActionBarActivity implements AdapterView.OnItemClickListener,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private Toolbar toolbar;
    private static final String TAG = "LocationActivity";
    private static long INTERVAL = 1000 * 60;
    private static long FASTEST_INTERVAL = 1000 * 10;
    TextView x;


    private AudioManager myAudioManager;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation, loc;
    String mLastUpdateTime;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    public static final String GET_IMAGE_URL = "http://myperiyar.esy.es/eventret.php";
    private ListView listView;

    public GetAlImage2 getAlImage;
    public static final String BITMAP_ID = "BITMAP_ID";
    private String idd;
    public static TextView nameid,libad;
    public static TextView regid;
    private LinearLayout header, dummy;
    public static ImageView profile;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;
    private ListView menu;
    private String[] Titles = {"Home", "My Library", "Recommend Book", "Lost & Found","Smart Reminder"};
    private String[] Subs = {"Spotlight & Current Affairs", "Explore Our Book Collections", "Suggest Books & Check Status", "Found Items in Library Premises","Keep Track of Issued Books"};
  //  private String[] Subs2 = {"Login Required !", "Login Required !", "Login Required !", "Login Required !"};
    private int[] Icons = {R.drawable.ic_home, R.drawable.ic_mylib, R.drawable.ic_recomm, R.drawable.ic_laf,R.drawable.ic_rem};
    //private int[] Icons2 = {R.drawable.ic_lock,R.drawable.ic_lock,R.drawable.ic_lock,R.drawable.ic_lock};
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        setContentView(R.layout.homecontainer);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else {
            showGPSDisabledAlertToUser();
        }

        loc = new Location("");
        loc.setLatitude(12.969377);
        loc.setLongitude(79.156938);
        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.ic_launcher);
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        lafCache.mod = myAudioManager.getRingerMode();
        x = (TextView) findViewById(R.id.tvLocation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        profile = (ImageView) findViewById(R.id.circleView);
        nameid = (TextView) findViewById(R.id.nameid);
        regid = (TextView) findViewById(R.id.regid);
        getSupportActionBar().setTitle("Spotlight");
        menu = (ListView) findViewById(R.id.slidermenu);
      //  if(lafCache.login) {
            CustomList2 customList = new CustomList2(Home.this, Titles, Icons, Subs);
            menu.setAdapter(customList);
      //  }else
        /*{
            CustomList2 customList = new CustomList2(Home.this, Titles, Icons2, Subs2);
            menu.setAdapter(customList);
        }*/

libad=(TextView)findViewById(R.id.libad);
        libad.setSelected(true);
        menu.setOnItemClickListener(this);
        nameid.setText(lafCache.nameid);
        regid.setText(lafCache.regid);
        profile.setImageBitmap(lafCache.profile);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        header = (LinearLayout) findViewById(R.id.header);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dummy = (LinearLayout) findViewById(R.id.dummy);
        dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//**********************************************************************************************************************

       if(lafCache.firsttime) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);

            } else {
                Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
            }

        }
//*********************************************************************************************************************
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed

            }


        };

        // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        listView = (ListView) findViewById(R.id.eventEntry);
        listView.setOnItemClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Authenticate();
                Intent abc = new Intent(Home.this, Authenticate.class);
                startActivity(abc);
                finish();
            }
        });

    ConnectivityManager connectivityManager
            = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
        getURLs();
    } else {
        Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
    }


    }

    //*****************************************MENU*******************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh1:

                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    getURLs();
                } else {
                    Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.Developer:
                Intent pqr = new Intent("com.periyar.reg13bce0722.app.DEVELOPER");
                startActivity(pqr);
                break;
            case R.id.setting:
                Intent xyz = new Intent("com.periyar.reg13bce0722.app.PREFS");
                startActivity(xyz);
                break;


            case R.id.Exit:
                finish();

                break;
        }

        return false;
    }

    //**************************************************************************************************************
    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == RESULT_OK) {
            String scanContent = intent.getStringExtra("SCAN_RESULT");
            String scanFormat = intent.getStringExtra("SCAN_RESULT_FORMAT");
            idd = scanContent;
            loginPost();


            //output to UI

        } else {
            //invalid scan data or scan canceled
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void loginPost() {
        String username = idd;
        new SigninActivity(this, false).execute(username);


    }


    private void getURLs() {
        class GetURLs extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Home.this, "Downloading Events Data...", "Please Wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s==null){
                    Toast.makeText(getBaseContext(), "Network Error!", Toast.LENGTH_LONG).show();
                }else {
                    getAlImage = new GetAlImage2(s);
                    try {
                        getAlImage.getAllImage();
                        if (GetAlImage2.len == 0) {
                            RelativeLayout x = (RelativeLayout) findViewById(R.id.noevent);
                            x.setBackgroundResource(R.drawable.noevent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    CustomList3 customList = new CustomList3(Home.this, GetAlImage2.imageURLs, GetAlImage2.id, GetAlImage2.head, GetAlImage2.foot, GetAlImage2.desc);
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
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute(GET_IMAGE_URL);
    }

    //*******************************************************SLIDER_MENU**********************************************
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.slidermenu) {
            switch (position) {
                case 0:
                        Drawer.closeDrawers();
                        ConnectivityManager connectivityManager
                                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                            getURLs();
                        } else {
                            Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                        }
                    break;
                case 3:
                    Drawer.closeDrawers();
                    if(lafCache.login) {
                    if (lafCache.cache) {
                        Intent b = new Intent("com.periyar.reg13bce0722.app.LAFCACHECON");
                        startActivity(b);

                    } else {
                        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                            Intent a = new Intent("com.periyar.reg13bce0722.app.IMAGELISTVIEW");
                            startActivity(a);

                        } else {
                            Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                        }
                    }} else {
                        new AlertDialog.Builder(Home.this)
                                .setTitle("Login Required")
                                .setMessage("Please Login First !")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        ConnectivityManager connectivityManager
                                                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                                            intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
                                            startActivityForResult(intent, 0);

                                        } else {
                                            Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })

                                .setIcon(R.drawable.ic_lock)
                                .show();

                    }
                    break;
                case 1:
                    Drawer.closeDrawers();
                    if(lafCache.login) {
                        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Intent k = new Intent("com.periyar.reg13bce0722.app.MYLIB");
                        startActivity(k);
                    } else {
                        Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                    }} else {
                        new AlertDialog.Builder(Home.this)
                                .setTitle("Login Required")
                                .setMessage("Please Login First !")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        ConnectivityManager connectivityManager
                                                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                                            intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
                                            startActivityForResult(intent, 0);

                                        } else {
                                            Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })

                                .setIcon(R.drawable.ic_lock)
                                .show();

                    }
                    break;

                case 2:
                    Drawer.closeDrawers();
                    if(lafCache.login) {
                        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Intent k = new Intent("com.periyar.reg13bce0722.app.RECOMMTAB");
                        startActivity(k);
                    } else {
                        Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                    }} else {
                        new AlertDialog.Builder(Home.this)
                                .setTitle("Login Required")
                                .setMessage("Please Login First !")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        ConnectivityManager connectivityManager
                                                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                                            intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
                                            startActivityForResult(intent, 0);

                                        } else {
                                            Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })

                                .setIcon(R.drawable.ic_lock)
                                .show();

                    }
                    break;
                case 4:
                    Drawer.closeDrawers();
                    if(lafCache.login) {
                        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                            Intent k = new Intent("com.periyar.reg13bce0722.app.SMARTREM");
                            startActivity(k);
                        } else {
                            Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                        }} else {
                        new AlertDialog.Builder(Home.this)
                                .setTitle("Login Required")
                                .setMessage("Please Login First !")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        ConnectivityManager connectivityManager
                                                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                                        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                                            intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
                                            startActivityForResult(intent, 0);

                                        } else {
                                            Toast.makeText(Home.this, "Network Connection Not Available!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })

                                .setIcon(R.drawable.ic_lock)
                                .show();

                    }
                    break;
            }
        }


        if (parent.getId() == R.id.eventEntry) {
            Intent intent = new Intent(this, ViewEvent.class);
            intent.putExtra(BITMAP_ID, position);
            startActivity(intent);
        }
        // break;

        //  }
    }
//*****************************************************************************************************************

    //******************************LOCATION FILE**************************************
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");

        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest,this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if(lafCache.penalty>=0){
            update();
        }
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            if (mCurrentLocation.distanceTo(loc) < 45) {
                lafCache.proxy = true;
                lafCache.status = true;
                lafCache.statCount = 0;
                Notify();
                if (lafCache.mod != AudioManager.RINGER_MODE_VIBRATE) {

                    myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
            } else {
                lafCache.proxy = false;
                lafCache.status = false;
                if (lafCache.statCount == 1) {
                    lafCache.mod = myAudioManager.getRingerMode();
                }
                if (lafCache.statCount == 0) {
                    Notify();
                    lafCache.statCount = 1;
                    if (lafCache.mod == AudioManager.RINGER_MODE_SILENT) {
                        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    } else if (lafCache.mod == AudioManager.RINGER_MODE_NORMAL) {
                        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    } else {
                        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    }
                }


            }

        /*    x.setText("At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Distance: " + mCurrentLocation.distanceTo(loc) + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy());*/
            x.setText("Approximate distance to Library : "+mCurrentLocation.distanceTo(loc)+" m \n(Depends on your device's GPS accuracy)");
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        INTERVAL = 1000 * 120;
        FASTEST_INTERVAL = 1000 * 60;
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            INTERVAL = 1000 * 60;
            FASTEST_INTERVAL = 1000 * 10;
            mLocationRequest.setInterval(INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d(TAG, "Location update resumed .....................");
        }
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

    //***************************************NOTIFICATION*************************************************
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void Notify() {
        NotificationManager mNotificationManager;
   /* Invoking the default notification service */
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        if (lafCache.status) {
            mBuilder.setContentTitle("Library Mode Status: ON");
        } else {
            mBuilder.setContentTitle("Library Mode Status: OFF");
        }
        mBuilder.setContentText("List of Missed Calls!");
        mBuilder.setTicker("Library Mode On");
        mBuilder.setSmallIcon(R.drawable.notify);


   /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(lafCache.count);

   /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();


        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Missed Calls !");
        inboxStyle.addLine("Tap For More Details!");
        // Moves events into the big view
        if (lafCache.count == 0) {
            mBuilder.setContentText("No Missed Calls!");
        }
        for (int i = 0; i < lafCache.count; i++) {

            inboxStyle.addLine(lafCache.CalledNo[i]);
        }

        mBuilder.setStyle(inboxStyle);

   /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://call_log/calls"));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addParentStack(LibModeNote.class);

   /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(1, mBuilder.build());
    }

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
private void update() {
    NotificationManager mNotificationManager;
   /* Invoking the default notification service */
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

    if (lafCache.penalty == 0) {
        mBuilder.setContentTitle("Return/Renew Book Today !");
    } else if (lafCache.penalty >= 1) {
        mBuilder.setContentTitle("Return Late by : " + lafCache.penalty+" day(s)");
    }
    mBuilder.setTicker("Issued Book Update");
    mBuilder.setSmallIcon(R.drawable.notify);
    Intent resultIntent = new Intent(getBaseContext(),SmartRem.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

    mBuilder.setContentIntent(resultPendingIntent);
    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
    mNotificationManager.notify(2, mBuilder.build());
}

}

