package com.example.taskdo;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Connection {


    public  String login(String username,String password){

        String url="http://192.168.0.2:80/testing/login.php";
        String status = null;
        try{
            URL object=new URL(url);

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("GET");


            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            //  wr.write("{\"username\":\""+username+"\", \"password\":\""+password+"\"}");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_name",username);
            jsonObject.put("user_password", password);
            wr.write(jsonObject.toString());
            wr.flush();

            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            System.out.println(HttpResult);
            InputStream iStream = con.getInputStream();
            Scanner sc = new Scanner(iStream);
            String response = sc.useDelimiter(",").next();
            System.out.println(response);
            iStream.close();
            System.out.println("" + sb.toString());
            JSONObject responsJson = new JSONObject(response);
            status = responsJson.getString("response");
        }catch(Exception e){
            //
        }

        return status;
    }

}
