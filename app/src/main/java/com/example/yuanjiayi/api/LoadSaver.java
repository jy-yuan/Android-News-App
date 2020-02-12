package com.example.yuanjiayi.api;
import com.example.yuanjiayi.models.*;

import android.content.Context;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.*;
import com.google.gson.reflect.TypeToken;

public class LoadSaver {
    public LoadSaver(){}

    public static void clear(Context context){
        ClearNews(context);
        ClearFav(context);
        ClearMap(context);
        ClearHisMap(context);
    }

    public static void ClearNews(Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"history");
        try{
            if(!file.exists()){
                return;
            }
            FileWriter writer = new FileWriter(file);
            writer.write("");
            writer.flush();

        }catch (Exception e){
            return;
        }
    }

    public static void ClearFav(Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"favorite");
        try{
            if(!file.exists()){
                return;
            }
            FileWriter writer = new FileWriter(file);
            writer.write("");
            writer.flush();

        }catch (Exception e){
            return;
        }
    }

    public static void ClearMap(Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"map");
        try{
            if(!file.exists()){
                return;
            }
            FileWriter writer = new FileWriter(file);
            writer.write("");
            writer.flush();

        }catch (Exception e){
            return;
        }
    }
    public static void ClearHisMap(Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"hismap");
        try{
            if(!file.exists()){
                return;
            }
            FileWriter writer = new FileWriter(file);
            writer.write("");
            writer.flush();

        }catch (Exception e){
            return;
        }
    }

    public static List<Article> Loadnews(Context context /**use getApplicationContext()*/){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"history");
        List<Article> artical = new ArrayList<Article>();
        try {
            if(!file.exists()){
                artical.add(new Article());
                artical.remove(0);
                return artical;
            }
            FileReader reader = new FileReader(file);
            StringBuilder buffer = new StringBuilder();
            int c;
            while((c = reader.read())!= -1){
                buffer.append((char)(c));
            }
            String json = buffer.toString();
            Gson gson = new Gson();
            artical = gson.fromJson(json,new TypeToken<List<Article>>() {
            }.getType());

        }catch (Exception e){
            System.out.println(e);
        }

        if(artical==null){
            artical = new ArrayList<>();
        }
        Collections.reverse(artical);
        return artical;
    }

    public static boolean Savenews(Article artical,Context context/**use getApplicationContext()*/){
        String path = "history";
        List<Article> articles = new ArrayList<Article>();
        if (artical.getState()== Article.STATE.HIS) {
            delfav(artical,context);
        } else {
            Savefav(artical,context);
        }
        articles = Loadnews(context);
        for(int i=0;i<articles.size();i++){
            if(artical.getNewsID().equals(articles.get(i).getNewsID())){
                articles.remove(i);
                break;
            }
        }
        articles.add(artical);
        saveHisMap(artical.getNewsID(), artical.getstrofs(),context);
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+path);
        boolean flag = true;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            String json = gson.toJson(articles);
            writer.write(json);
            writer.flush();
        }catch (Exception e){
            System.out.println(e);
            flag = false;
        }
        HashMap map = LoadSaver.LoadMap(User.getname()+"map",context);
        for(int i=0;i<artical.getKeywordList().size();i++){
            String s = artical.getKeywordList().get(i).getWord();
            if(map.containsKey(s)) {
                double newscore = (double)map.get(artical.getKeywordList().get(i).getWord()) + artical.getKeywordList().get(i).getScore();
                map.put(s,newscore);
            }else{
                map.put(s,artical.getKeywordList().get(i).getScore());
            }
        }
        LoadSaver.saveMap(map,User.getname()+"map",context);
        return flag;
    }

    public static List<Article> Loadfav(Context context /**use getApplicationContext()*/){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"favorite");
        List<Article> artical = new ArrayList<Article>();
        try {
            if(!file.exists()){
                artical.add(new Article());
                artical.remove(0);
                return artical;
            }
            FileReader reader = new FileReader(file);
            StringBuilder buffer = new StringBuilder();
            int c;
            while((c = reader.read())!= -1){
                buffer.append((char)(c));
            }
            String json = buffer.toString();
            Gson gson = new Gson();
            artical = gson.fromJson(json,new TypeToken<List<Article>>() {
            }.getType());
        }catch (Exception e){
            System.out.println(e);
        }

        if(artical==null){
            artical = new ArrayList<>();
        }
        Collections.reverse(artical);
        return artical;
    }

    public static boolean Savefav(Article artical,Context context){
        List<Article> articles = Loadfav(context);
        for(int i=0;i<articles.size();i++){
            if(artical.getNewsID().equals(articles.get(i).getNewsID())){
                articles.remove(i);
                break;
            }
        }
        articles.add(artical);
        saveHisMap(artical.getNewsID(), "FAV",context);
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"favorite");
        boolean flag = true;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            String json = gson.toJson(articles);
            writer.write(json);
            writer.flush();
        }catch (Exception e){
            System.out.println(e);
            flag = false;
        }
        return flag;
    }
    public static boolean Savenews(List<Article> artical,Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"history");
        boolean flag = true;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            String json = gson.toJson(artical);
            writer.write(json);
            writer.flush();
        }catch (Exception e){
            System.out.println(e);
            flag = false;
        }
        return flag;
    }

    public static boolean Savefav(List<Article> artical,Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"favorite");
        boolean flag = true;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            String json = gson.toJson(artical);
            writer.write(json);
            writer.flush();
        }catch (Exception e){
            System.out.println(e);
            flag = false;
        }
        return flag;
    }

    public static boolean delfav(Article artical,Context context/**use getApplicationContext()*/){
        List<Article> articles = Loadfav(context);
        for(int i=0;i<articles.size();i++){
            if(artical.getNewsID().equals(articles.get(i).getNewsID())){
                articles.remove(i);
                break;
            }
        }
        saveHisMap(artical.getNewsID(), "HIS",context);
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"favorite");
        boolean flag = true;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            String json = gson.toJson(articles);
            writer.write(json);
            writer.flush();
        }catch (Exception e){
            System.out.println(e);
            flag = false;
        }
        return flag;
    }

    public static HashMap<String,String> LoadHisMap(Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"hismap");
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("","");
        map.clear();
        try {
            if(!file.exists()){
                return map;
            }
            FileReader reader = new FileReader(file);
            StringBuilder buffer = new StringBuilder();
            int c;
            while((c = reader.read())!= -1){
                buffer.append((char)(c));
            }
            String json = buffer.toString();
            Gson gson = new Gson();
            map = gson.fromJson(json,HashMap.class);
            System.out.println(map);
        }catch (Exception e){
            System.out.println(e);
        }
        if(map==null){
            map = new HashMap<>();
        }
        return map;
    }
    public static boolean saveHisMap(String ID,String state,Context context){
        Map map = LoadHisMap(context);
        map.put(ID,state);
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"hismap");
        boolean flag = true;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            String json = gson.toJson(map);
            writer.write(json);
            writer.flush();
        }catch (Exception e){
            System.out.println(e);
            flag = false;
        }
        return flag;
    }

    public static HashMap LoadMap(String path,Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+path);
        HashMap map = new HashMap();
        map.put("","");
        map.clear();
        try {
            if(!file.exists()){
                return map;
            }
            FileReader reader = new FileReader(file);
            StringBuilder buffer = new StringBuilder();
            int c;
            while((c = reader.read())!= -1){
                buffer.append((char)(c));
            }
            String json = buffer.toString();
            Gson gson = new Gson();
            map = gson.fromJson(json,HashMap.class);
            System.out.println(map);
        }catch (Exception e){
            System.out.println(e);
        }

        if(map==null){
            map = new HashMap<>();
        }
        return map;
    }
    public static void saveblock(List<String> blockList,Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"block");
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            String json = gson.toJson(blockList);
            writer.write(json);
            writer.flush();
        }catch (Exception e){

        }
    }

    public static void savelist(List<LogInfo> blockList,String path,Context context){
        File  file = new File(context.getExternalCacheDir().getPath().toString()+path);
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            String json = gson.toJson(blockList);
            writer.write(json);
            writer.flush();
        }catch (Exception e){

        }
    }
    public static List<String> Loadblock(Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"block");
        List<String> blockList = new ArrayList<String>() ;
        blockList.add("");
        blockList.clear();
        try {
            if(!file.exists()){
                return blockList;
            }
            FileReader reader = new FileReader(file);
            StringBuilder buffer = new StringBuilder();
            int c;
            while((c = reader.read())!= -1){
                buffer.append((char)(c));
            }
            String json = buffer.toString();
            Gson gson = new Gson();
            blockList = gson.fromJson(json,new TypeToken<List<String>>(){}.getType());
            //System.out.println(blockList);
        }catch (Exception e){
            System.out.println(e);
        }
        if(blockList==null){
            blockList = new ArrayList<>();
        }
        return blockList;
    }
    public static List<LogInfo> Loadlist(String path,Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+path);
        List<LogInfo> blockList = new ArrayList<LogInfo>() ;
        blockList.add(new LogInfo("",""));
        blockList.clear();
        try {
            if(!file.exists()){
                return blockList;
            }
            FileReader reader = new FileReader(file);
            StringBuilder buffer = new StringBuilder();
            int c;
            while((c = reader.read())!= -1){
                buffer.append((char)(c));
            }
            String json = buffer.toString();
            Gson gson = new Gson();
            blockList = gson.fromJson(json,new TypeToken<List<LogInfo>>(){}.getType());
            //System.out.println(blockList);
        }catch (Exception e){
            System.out.println(e);
        }
        if(blockList==null){
            blockList = new ArrayList<>();
        }
        return blockList;
    }

    public static boolean saveMap(HashMap map,String path,Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+path);
        boolean flag = true;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            Gson gson = new Gson();
            String json = gson.toJson(map);
            writer.write(json);
            writer.flush();
        }catch (Exception e){
            System.out.println(e);
            flag = false;
        }
        return flag;
    }

    public static void saveCate(Boolean[] c,Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"category");
        Boolean flag = true;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            for(int i=0;i<10;i++){
                if(c[i])
                    writer.write(1);
                else
                    writer.write(0);
            }
            writer.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static Boolean[] LoadCate(Context context){
        File file = new File(context.getExternalCacheDir().getPath().toString()+User.getname()+"category");
        List<String> blockList = new ArrayList<String>() ;
        try {
            if(!file.exists()){
                Boolean[] bool = {true, true, true, true, true, true, true, true, true, true};
                return bool;
            }
            Boolean cate[] = new Boolean[10];
            FileReader reader = new FileReader(file);
            StringBuilder buffer = new StringBuilder();
            int c;
            int i=0;
            while((c = reader.read())!=-1){
                if(c==0){
                    cate[i] = false;
                }else{
                    cate[i] = true;
                }
                i++;
            }
            return cate;
        }catch (Exception e){
            System.out.println(e);
        }
        Boolean[] bool = {true, true, true, true, true, true, true, true, true, true};
        return bool;
    }
}