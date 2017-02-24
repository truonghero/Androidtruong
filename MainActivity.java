package com.example.hatrang.networkdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView txt;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt=(TextView)findViewById(R.id.txt);
        txt.setMovementMethod(new ScrollingMovementMethod());

        if (checkInternet()){
            txt.setText("Connected");
            Toast.makeText(this,"Connected",Toast.LENGTH_LONG).show();
        }else{
            txt.setText("Not connected. Check your internet.");
            Toast.makeText(this,"not connect",Toast.LENGTH_LONG).show();
        }

        //cach 1: dung AsyncTask
        //new myAsyncTask().execute("https://google.com.vn");

        //cach 2: dung Volley
        //Buoc 1: tao RequestQUEUE - hang doi
        queue= Volley.newRequestQueue(this);

        //Buoc 2: tao request
        StringRequest request=new StringRequest(Request.Method.GET, "https://google.com.vn",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        txt.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txt.setText("Loi khong lay duoc du lieu");
                    }
                });

        //Buoc 3: them request vao hang doi RequestQueue
        queue.add(request);
    }


    public void movetoActivity2(View view) {
        Intent i=new Intent(this,Main2Activity.class);
        startActivity(i);
    }

    //tao inner class AsyncTask
    class myAsyncTask extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(MainActivity.this,"Loading....","Please wait",true,false);
        }

        @Override
        protected String doInBackground(String... strings) {
            return loadWeb(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            txt.setText(s);
        }
    }



    private String loadWeb(String link) {
        StringBuilder builder=new StringBuilder();
        try {
            //mo url toi web
            URL url=new URL(link);
            //mo ket noi
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.connect();

            //lay ra luong doc InputStream
            InputStream is=conn.getInputStream();

            //vi doc ky tu nen su dung lop InputStreamReader boc ngoai
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));


            String str;

            //doc tung dong cho toi het - luu vao StringBuilder
            while ((str=reader.readLine()) != null){
                builder.append(str);
            }

        } catch (MalformedURLException e) {
            Toast.makeText(this,"Khong tim thay website. Kiem tra lai duong link",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private boolean checkInternet(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=connectivityManager.getActiveNetworkInfo();
        if (activeNetwork !=null && activeNetwork.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }
}
