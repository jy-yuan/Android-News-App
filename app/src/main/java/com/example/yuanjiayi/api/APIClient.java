package com.example.yuanjiayi.api;

import android.content.Context;

import com.example.yuanjiayi.models.UInfo;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

public class APIClient {
    private static String Json;
    private static boolean flag = false;

    public APIClient() {

    }

    public static void getJson(final String request, final int i) {
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(request);
                    Json = new Scanner(url.openStream()).useDelimiter("\\A").next();
                    Searcher.Searchcall.callback(Json, i);
                    //System.out.println(Json);
                } catch (Exception e) {
                    System.out.println("???" + e);
                }
                flag = true;
            }
        }.start();
    }

    public static void setLog(final int id, final UInfo uInfo) {
        new Thread() {
            public void run() {
                try {
                    URL url = new URL("183.172.152.148:8000/api/Values/");
                    Gson gson = new Gson();
                    String json = gson.toJson(uInfo);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();
    }

    public static void Post(final int i,final String json) {
        new Thread() {
            public void run() {
                try {
                    //json.replaceAll("[\\]","\\");
                    String json2 = string2Json(json);
                    //System.out.println(json2);
                    byte[] data = json2.getBytes();
                    URL url = new URL("http://183.172.152.148:8000/api/Values?i="+i);
                    if(i==-1){
                        url = new URL("http://183.172.152.148:8000/api/Values");
                    }
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(10000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Content-Length", String.valueOf(data.length));
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(data);

                    int response = connection.getResponseCode();
                    if (response == HttpURLConnection.HTTP_OK || response==204) {
                        System.out.println("success");// if ok// save settings
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    } else { // error
                        // do nothing
                        System.out.println(response);
                        System.out.println("failed");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();
    }

    public static void Get(final int i, final Callback c, final Context context) {
        new Thread() {
            public void run() {
                try {
                    URL url = new URL("http://183.172.152.148:8000/api/Values?i="+i);
                    if(i==-1){
                        url = new URL("http://183.172.152.148:8000/api/Values");
                    }
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //默认值我GET
                    con.setRequestMethod("GET");
                    //添加请求头
                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'GET' request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    Gson gson = new Gson();
                    String json = json2string(response.toString());
                    System.out.println(json);
                    c.call(i,json,context);
                } catch (Exception e) {
                }

            }
        }.start();
    }

    static String string2Json(String s) {
        StringBuffer sb = new StringBuffer();
        sb.append("\"");
        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        sb.append("\"");
        return sb.toString();
    }



    public static String json2string(String s){
        if(s==null || s.length()<3){
            return "";
        }
        s = s.substring(1,s.length()-1);
        s = s.replace("\\\"","\"");
        s = s.replace("\\n","\n");
        s = s.replace("\\r","\r");
        s = s.replace("\\t","\t");
        s = s.replace("\\f","\f");
        s = s.replace("\\b","\b");
        s = s.replace("\\/","/");
        s = s.replace("\\n","\n");
        s = s.replace("\\\"","\"");
        s = s.replace("\\\\","\\\\");
        return s;
    }
}

