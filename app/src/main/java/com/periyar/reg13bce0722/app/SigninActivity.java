package com.periyar.reg13bce0722.app;

/**
 * Created by Shubham on 09/12/2015.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class
        SigninActivity extends AsyncTask<String, Void, String> {
    Boolean fromAut;
    private Context context;
    public String reg1, name1, role1;

    Bitmap bitmap;
    ProgressDialog pDialog;
    ProgressDialog loading;

    public SigninActivity(Context context, Boolean fromAut) {
        this.context = context;
        this.fromAut = fromAut;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(context, "Fetching...", "Wait...", false, false);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String username = (String) arg0[0];

            String link = "http://xyz.php?Reg_id=" + username;


            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }


    @Override
    protected void onPostExecute(String result) {
        loading.dismiss();
        if (result == null) {
            Toast.makeText(context, "Network Error!", Toast.LENGTH_LONG).show();
        }else{
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<:" + result + ":>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray result1 = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result1.getJSONObject(0);
            reg1 = c.getString(Config.TAG_REG);
            name1 = c.getString(Config.TAG_NAME);
            role1 = c.getString(Config.TAG_ROLE);
            String dtStart=c.getString("Record");
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd");
if(!dtStart.equals("0000-00-00")) {
    try {

        Date oldDate = dateFormat.parse(dtStart);
        System.out.println(oldDate);

        Date currentDate = new Date();

        long diff = currentDate.getTime() - oldDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 14) {
            lafCache.penalty = (int) (days - 14);
        } else if (days < 14) {
        } else if (days == 14) {
            if (lafCache.penalty == -1) {
                lafCache.penalty = 0;
            }
        }


    } catch (ParseException e) {

        e.printStackTrace();
    }
}
          
            String photo1="http://xyz.php?Reg_id="+reg1;

            if (!reg1.trim().equals("null")) {
                if (fromAut) {
                    Authenticate.reg.setText(" RegID : " + reg1);
                    Authenticate.name.setText(" Name  : " + name1);
                    Authenticate.role.setText(" Role  : " + role1);
                }
                lafCache.nameid = name1;
                lafCache.regid = reg1;
                lafCache.roleid = role1;
                Home.nameid.setText(lafCache.nameid);
                Home.regid.setText(lafCache.regid);
                lafCache.firsttime = false;
                lafCache.login = true;

                new LoadImage().execute(photo1);


            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Invalid Barcode")
                        .setMessage("User is not enrolled in trial database!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Finalizing ....");
            pDialog.show();

        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if (image != null) {
                if (fromAut) {
                    Authenticate.photo.setImageBitmap(image);
                }
                lafCache.profile = image;
                Home.profile.setImageBitmap(lafCache.profile);
                pDialog.dismiss();

                String wel = "Welcome " + name1 + " !";
                Toast.makeText(context, wel, Toast.LENGTH_LONG).show();

            } else {

                pDialog.dismiss();

                Toast.makeText(context, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }


}
