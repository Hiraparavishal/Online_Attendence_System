package com.example.camera1;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class GetService extends AsyncTask<String , Void ,String> {

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                System.out.println("read data line  : " +line);
                sb.append(line).append('\n');
            }
        } catch (IOException e) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }
        return sb.toString();
    }

    @Override
    protected String doInBackground(String... strings) {
        System.out.println("URL IN SERVICE : " +strings[0]);
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = urlConnection.getInputStream();
            return convertStreamToString(is);
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }
        return null;
    }
}
