package com.example.yuanjiayi.models;

import android.content.Context;

import com.example.yuanjiayi.api.APIClient;
import com.example.yuanjiayi.api.Callback;
import com.example.yuanjiayi.api.LoadSaver;
import com.google.gson.Gson;

import java.util.List;

public  class User {
    private static String name = "";
    public static String getname(){
        return name;
    }
    public static UInfo info;
    public static void init(Context context){
        APIClient.Get(-1,new Callback(),context);
        info = new UInfo("","naivenews");
    }
    public static void login(String _name,Context context){
        if(name == ""){
            LoadSaver.clear(context);
        }
        name = _name;
        List<LogInfo> list = LoadSaver.Loadlist("log",context);
        int i=-1;
        for(int j=0;j<list.size();j++) {
            if(list.get(j).name.equals(name)){
                i = j;
                break;
            }
        }
        APIClient.Get(i, new Callback(), context);
    }
    public static void logout(Context context){
        SendUInfo(context);
        name = "";
    }
    public static void SendUInfo(Context context){
        List<LogInfo> list = LoadSaver.Loadlist("log",context);
        int i=-1;
        for(int j=0;j<list.size();j++) {
            if(list.get(j).name.equals(name)){
                i = j;
                break;
            }
        }
        APIClient.Get(i, new Callback(), context);
        if(i<0){
            return;
        }
        info.name = name;
        info.psw = (String)LoadSaver.LoadMap("log",context).get(name);
        info.favList = LoadSaver.Loadfav(context);
        info.newsList = LoadSaver.Loadnews(context);
        info.BlockList = LoadSaver.Loadblock(context);
        info.hismap = LoadSaver.LoadMap(name+"hismap",context);
        info.map = LoadSaver.LoadMap(name+"map",context);
        info.categories = LoadSaver.LoadCate(context);
        Gson gson = new Gson();
        String json = gson.toJson(info);
        APIClient.Post(i,json);
    }
}
