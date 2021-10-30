package com.toilet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpUtil {
    public static String AuthorizationToken = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySUQiOjExLCJjcmVhdGVUaW1lIjoxNjM1MzUxNTY2OTExLCJ1c2VybmFtZSI6IndpbGwiLCJleHAiOjE2MzU1MzE1NjZ9.ju9NG_NKeV30dsbpcSZ12HZNtPNUCKezlGTBsvtmjmuz_g3jCl0PBHELZxQd5AmAGOEgxUVTXv8pJ0U68EhH8g";

    public  static  String Post(String url, String body){

        Logger.getLogger("Toilet").log(Level.INFO, "damagedtoilet post body:"+body);

        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestProperty("Authorization", AuthorizationToken);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            OutputStream os = connection.getOutputStream();
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;



    }

    public  static  String Delete(String url){
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", AuthorizationToken);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }
    public static String Get(String url){

        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", AuthorizationToken);
            if (connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                return br.readLine();
            }
            return null;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



}
