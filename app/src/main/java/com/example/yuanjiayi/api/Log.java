package com.example.yuanjiayi.api;

import android.content.Context;

import com.example.yuanjiayi.models.LogInfo;
import com.google.gson.Gson;

import java.util.List;

public class Log {
    public static boolean signin(String name, String password, Context context){
        List<LogInfo> list = LoadSaver.Loadlist("log",context);
        for(int i=0;i<list.size();i++){
            if(list.get(i).name.equals(name))
                return false;
        }
        list.add(new LogInfo(name,password));
        LoadSaver.savelist(list,"log",context);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        APIClient.Post(-1,json);
        return true;
    }
    public static int login(String name,String password,Context context){
        List<LogInfo> list = LoadSaver.Loadlist("log",context);
        for(int i=0;i<list.size();i++){
            if(list.get(i).name.equals(name)){
                if(list.get(i).psw.equals(password)){
                    return 0;
                }
                return 2;
            }
        }
        return 1;
    }
}
