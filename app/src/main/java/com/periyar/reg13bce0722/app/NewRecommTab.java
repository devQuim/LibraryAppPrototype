package com.periyar.reg13bce0722.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Shubham on 28/12/2015.
 */
public class NewRecommTab extends android.support.v4.app.Fragment {
    public Button submitRecomm;
    public EditText title,author,isbn,publisher,copies,remarks;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.newrecommtab, container, false);
        title=(EditText)v.findViewById(R.id.Title);
        author=(EditText)v.findViewById(R.id.Author);
        isbn=(EditText)v.findViewById(R.id.ISBN);
        publisher=(EditText)v.findViewById(R.id.Publisher);
        copies=(EditText)v.findViewById(R.id.Copies);
        remarks=(EditText)v.findViewById(R.id.Remarks);
        submitRecomm=(Button)v.findViewById(R.id.submitRecomm);
        submitRecomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lafCache.regid.equals("Reg. No.")) {
                    Toast.makeText(getView().getContext(), "Login First !", Toast.LENGTH_SHORT).show();
                } else if (title.getText().toString().trim().equals("") || author.getText().toString().trim().equals("") || publisher.getText().toString().trim().equals("") || copies.getText().toString().trim().equals("")) {
                    Toast.makeText(getView().getContext(), "Please Fill Up All Compulsory Fields!", Toast.LENGTH_SHORT).show();
                } else {
                    addRecomm();
                }
            }
        });
        return v;
    }

    private void addRecomm() {

        final String title1 = title.getText().toString().trim().toUpperCase().replace("'", "\\'");
        final String author1 = author.getText().toString().trim().toUpperCase().replace("'", "\\'");
        final String isbn1 = isbn.getText().toString().trim();
        final String publisher1 = publisher.getText().toString().trim().toUpperCase().replace("'","\\'");
        final String copies1 = copies.getText().toString().trim();
        final String remarks1 =remarks.getText().toString().trim().replace("'", "\\'");
System.out.println(title1);
        class AddRecomm extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getView().getContext(),"Processing...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s==null){
                    Toast.makeText(getView().getContext(), "Network Error!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getView().getContext(), s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("RecommBy",lafCache.regid);
                params.put("Title",title1);
                params.put("Author",author1);
                params.put("ISBN",isbn1);
                params.put("Publisher",publisher1);
                params.put("Copies",copies1);
                params.put("Remarks",remarks1);
                String res = sendPostRequest("http://myperiyar.esy.es/Recomm.php", params);
                return res;
            }
        }

        AddRecomm ae = new AddRecomm();
        ae.execute();

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
}
