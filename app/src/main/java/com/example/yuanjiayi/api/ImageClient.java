package com.example.yuanjiayi.api;

import java.io.InputStream;
import java.net.*;
import android.graphics.*;

public class ImageClient {
    private String URLRequest;
    private InputStream inputStream;
    private boolean flag;
    private Bitmap bitmap;

    public ImageClient(String Request) {
        URLRequest = Request;
        flag = false;
    }

    public Bitmap getImage() {
        if (URLRequest.equals("") || URLRequest == null) {
            return null;
        }
        new Thread() {
            public void run() {
                try {
                    System.out.println(URLRequest+"000");
                    URL url = new URL(URLRequest);                    //服务器地址
                    if (url != null) {
                        //打开连接
                        inputStream = url.openStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                flag = true;
            }
        }.start();
        while (!flag) {
        }

        return bitmap;
    }
}
