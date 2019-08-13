package com.example.camera1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.droidsonroids.gif.GifImageView;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        GifImageView g1=(GifImageView)findViewById(R.id.gif);
        g1.setImageResource(R.drawable.load);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            start();
        } else {

            Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(), "PLEASE START INTERNET CONNECTION \n" +
                            " AFTER RESTART APPLICATION", Toast.LENGTH_LONG).show();
                }
            },2000);


            connected = false;
        }


    }

    public  void start()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent lo=new Intent(splash.this,login.class);
                startActivity(lo);
                finish();

            }
        },3000);

    }
    }




