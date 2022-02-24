package com.example.taskdo;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetData extends AsyncTask {
    String url,url2 ;
    String baseUrl = "http://192.168.1.6//testing/";
    public GetData(String url){
        this.url = url;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String res="none";
        url2 = baseUrl+url;
        try {
            URL u = new URL(url2);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line ;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            res = sb.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
