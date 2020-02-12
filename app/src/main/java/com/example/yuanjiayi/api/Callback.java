package com.example.yuanjiayi.api;

import android.content.Context;

import com.example.yuanjiayi.models.LogInfo;
import com.example.yuanjiayi.models.UInfo;
import com.example.yuanjiayi.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Callback {
    public void call(int i,String json, Context context){
        System.out.println("0: "+json);
        if(i>0) {
            Gson gson = new Gson();
            UInfo info = gson.fromJson(json, UInfo.class);
            LoadSaver.Savefav(info.getFavList(), context);
            LoadSaver.Savenews(info.getNewsList(), context);
            LoadSaver.saveMap(info.getMap(), User.getname() + "map", context);
            LoadSaver.saveMap(info.getHismap(), User.getname() + "hismap", context);
            LoadSaver.saveblock(info.getBlockList(), context);
            LoadSaver.saveCate(info.getCategories(), context);
        }else{
            Gson gson = new Gson();
            List<LogInfo> list = gson.fromJson(json,new TypeToken<List<LogInfo>>(){}.getType());
            System.out.println();
            LoadSaver.savelist(list,"log",context);
        }
    }
}

