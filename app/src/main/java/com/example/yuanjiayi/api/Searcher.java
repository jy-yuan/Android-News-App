package com.example.yuanjiayi.api;

import android.content.Context;

import com.example.yuanjiayi.MainActivity;
import com.example.yuanjiayi.models.*;
import com.google.gson.Gson;
import java.util.*;

public class Searcher {
    static String API = "https://api2.newsminer.net/svc/news/queryNewsList?";
    static String size_Token = "size=";
    static String startDate_Token = "startDate=";
    static String endDate_Token = "endDate=";
    static String keyWord_Token = "words=";
    static String category_Token = "categories=";
    static String connection_Token = "&";
    static String page_Token = "page=";
    static Map map;
    public static List<Article> articles = new ArrayList<Article>();
    public static List<String> blockList = new ArrayList<String>();
    public static Context context;
    public static class Searchcall{
        public static MainActivity.CallBack yourcallback;
        public static Recommender.RecommendCall Reccallback;
        public Searchcall(MainActivity.CallBack c){
            yourcallback = c;
        }
        public static void callback(String json,int selection){
            articles = getArticlesfromjson(json);
            articles = block(articles);
            getState(articles,LoadSaver.LoadHisMap(context));
            switch (selection) {
                case 0:
                    yourcallback.call(articles);
                    break;
                case 1:
                    Recommender.RecommendCall.callback(articles);
            }
        }
    }
    public static List<Article> getArticlesfromjson(String json){
        Gson gson = new Gson();
        News news = gson.fromJson(json,News.class);
        return news.getArticle();
    }

    public static List<Article> block(List<Article> articles){
        List<Article> finalList = new ArrayList<>();
        for(int i=0;i<articles.size();i++){
            List<String> keyList = articles.get(i).getKeys();
            keyList.retainAll(blockList);
            if(keyList.isEmpty()){
                finalList.add(articles.get(i));
            }
        }
        return finalList;
    }

    public static void Search(String words, String categories, int page, MainActivity.CallBack c){
        String request = API
                +keyWord_Token+words+connection_Token
                +category_Token+categories+connection_Token
                +endDate_Token+"2020-01-01"+connection_Token
                +page_Token+page;
        Searchcall.yourcallback = c;
        APIClient.getJson(request,0);
    }

    public static void RecSearch(String words){
        String request = API
                +keyWord_Token+words+connection_Token
                +endDate_Token+"2020-01-01"+connection_Token
                +size_Token+"10";
        APIClient.getJson(request,1);
    }
    /*public static List<Article> Search(SearchText text,List<String> blockList){
        String request = API
                +keyWord_Token+text.getWords()+connection_Token
                +size_Token+text.getSize()+connection_Token
                +category_Token+text.getCategory()+connection_Token
                +startDate_Token+text.getStartDate()+connection_Token
                +endDate_Token+text.getEndDate();
        int page = 1;
        int size = 0;
        List<Article> articalList = new ArrayList<Article>();
        while(page<=10){
            System.out.println(page+": what are you doing?");
            page++;
            String request_page = request+connection_Token+page_Token+page;
            System.out.println("connecting...");
            News news = getFromRequest(request_page);
            System.out.println("connected, news size is "+news.getArticle().size());
            if(size<text.getSize()){
                List<Article> newList = news.getArticle();
                List<Article> delList = new ArrayList<Article>();
                for(int i=0;i<newList.size();i++) {
                    List<String> keyList = newList.get(i).getKeys();
                    keyList.retainAll(blockList);
                    if(!keyList.isEmpty()){
                        delList.add(newList.get(i));
                    }
                }
                newList.removeAll(delList);
                articalList.addAll(newList);
                size += newList.size();
            }else{
                articalList = articalList.subList(0,text.getSize());
                System.out.println("do you forget to break?");
                break;
            }
        }
        System.out.println("search success");
        return articalList;
    }

    public static List<Article> Search(String words,List<String> blockList){
        String request = API
                +keyWord_Token+words+connection_Token
                +endDate_Token+"2020-01-01"+connection_Token
                +size_Token+"10";
        int page = 1;
        int size = 0;
        List<Article> articalList = new ArrayList<Article>();
        while(page<=10){
            page++;
            String request_page = request+connection_Token+page_Token+page;
            News news = getFromRequest(request_page);
            if(size<10){
                List<Article> newList = news.getArticle();
                List<Article> delList = new ArrayList<Article>();
                for(int i=0;i<newList.size();i++) {
                    List<String> keyList = newList.get(i).getKeys();
                    keyList.retainAll(blockList);
                    if(!keyList.isEmpty()){
                        delList.add(newList.get(i));
                    }
                }
                newList.removeAll(delList);
                articalList.addAll(newList);
                size += newList.size();
            }else{
                articalList = articalList.subList(0,10);
                break;
            }
        }
        System.out.println(articalList.size());
        return articalList;
    }
    public static List<Article> Search(String words,int page,List<String> blockList){
        String request = API
                + keyWord_Token+words+connection_Token
                +endDate_Token+"2020-01-01"+connection_Token
                +page_Token+page;
        List<Article> newList = getFromRequest(request).getArticle();
        List<Article> finalList = new ArrayList<Article>();
        for(int i=0;i<newList.size();i++) {
            List<String> keyList = newList.get(i).getKeys();
            keyList.retainAll(blockList);
            if(keyList.isEmpty()){
                finalList.add(newList.get(i));
            }
        }
        return finalList;
    }
    public static List<Article> Search(SearchText text,int page,List<String> blocklist){
        String request = API
                +keyWord_Token+text.getWords()+connection_Token
                +category_Token+text.getCategory()+connection_Token
                +startDate_Token+text.getStartDate()+connection_Token
                +endDate_Token+text.getEndDate()+connection_Token
                +page_Token+page;
        List<Article> articalList = getFromRequest(request).getArticle();
        return articalList;
    }
    public static News getFromRequest(String request){
        //System.out.println(request);
        String json = APIClient.getJson(request);
        Gson gson = new Gson();
        News news = gson.fromJson(json,News.class);
        return news;
    }*/
    public static List<Article> getState(List<Article> list,HashMap<String, String> map){
        for (int i =0;i<list.size();i++){
            if(map.containsKey(list.get(i).getNewsID())) {
                //System.out.println(map.get(list.get(i).getNewsID()));
                Article a = list.get(i);
                a.setState(map.get(list.get(i).getNewsID()));
                list.set(i,a);
            }else{
                Article a = list.get(i);
                a.setState("NEW");
                list.set(i,a);
            }
        }
//        for(int i=0;i<list.size();i++){
//            System.out.println(list.get(i).getState());
//        }
        return list;
    }

}
