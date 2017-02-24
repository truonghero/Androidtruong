package com.example.hatrang.networkdemo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {
    String link="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLNujtq98DJlF2PcVUL_lHB9KS9mgbrULv_fUIdRYFDaseHPyddLqMhpg";
    String link1="http://iheartdogs.com/wp-content/uploads/2015/01/Screenshot-2015-01-17-16.15.29.png";
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        img= (ImageView) findViewById(R.id.img);
    }

    public void getPix(View view) {
        //Cach 1: Dung AsyncTask
        //new myAsyncTask().execute(link1);
        //Cach 2: Dung Volley
        //tao queue
        RequestQueue queue= Volley.newRequestQueue(this);

        //Buoc 2: tao request
        ImageRequest request=new ImageRequest(link1,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                },
                0,0, ImageView.ScaleType.FIT_CENTER,null,
        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("hatrang","Loi khong lay duoc du lieu");
                    }
                });

        //Buoc 3: them request vao hang doi RequestQueue
        queue.add(request);
    }

    //tao inner class AsyncTask
    class myAsyncTask extends AsyncTask<String,Void,Bitmap> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(Main2Activity.this,"Loading....","Please wait",true,false);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return loadPix(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            progressDialog.dismiss();
            img.setImageBitmap(b);
        }
    }



    private Bitmap loadPix(String link) {
        Bitmap b=null;
        try {
            //mo url toi web
            URL url=new URL(link);
            //mo ket noi
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.connect();

            //lay ra luong doc InputStream
            InputStream is=conn.getInputStream();

            b= BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            Toast.makeText(this,"Khong tim thay website. Kiem tra lai duong link",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}
