package com.example.yuanjiayi.api;
import com.example.yuanjiayi.MainActivity;
import com.example.yuanjiayi.models.*;
import java.util.*;

import android.content.Context;

public class Recommender {
    public static double RECOMMEND_SCORE = (double)1e-4;
    public static Map NOW_PAGE = new HashMap<String,Integer>();
    public static int KEY_NUM = 7;
    public static int num;
    public static Map newsmap = new HashMap<String,String>();
    public static List articleList = new ArrayList<Article>();
    public static List allArticles = new ArrayList<Article>();
    public Recommender(){}

    public static class RecommendCall{
        public static void callback(List<Article> articles){
            if(articles !=null && articles.size()>=1){
                int sum=0;
                System.out.println("re: back "+num);
                for(int i=0;i<articles.size();i++){
                    String s = articles.get(i).getNewsID();
                    if(Recommender.check(articles.get(i))){
                        newsmap.put(s,s);
                        articleList.add(articles.get(i));
                        if(++sum==2){
                            break;
                        }
                    }
                }
            }
            num++;
            if(num == KEY_NUM){
                num=0;
                //System.out.println(articleList.size()+"\n"+articleList.get(0));
                System.out.println("re: calling "+ Searcher.Searchcall.yourcallback);
                Searcher.Searchcall.yourcallback.call(articleList);
                System.out.println("re: call success");
            }
        }
    }
    public static boolean check(Article article){
        if(!newsmap.containsKey(article.getNewsID()) && article.getstrofs()=="NEW")
            return true;
        return false;
    }

    public static void getnextRecommend(Context context, MainActivity.CallBack c){
        System.out.println("re: next");
        articleList.clear();
        Searcher.Searchcall.yourcallback = c;
        Map map =  LoadSaver.LoadMap("map",context);
        if(map==null || map.isEmpty() || map.size()<KEY_NUM){
            Searcher.Search("","",(int)NOW_PAGE.get(""),c);
        }else{
            List<String> words = getwordsbyp(map);
            for(int i=0;i<KEY_NUM;i++) {
                String word = words.get(i);
                Searcher.RecSearch(word);
            }
        }
    }
    public static void getlatestRecommend(Context context, MainActivity.CallBack c){
        System.out.println("re: late");
        newsmap.clear();
        articleList.clear();
        Searcher.Searchcall.yourcallback = c;
        Map map =  LoadSaver.LoadMap("map",context);
        if(map==null || map.isEmpty() || map.size()<KEY_NUM){
            Searcher.Search("","",1,c);
        }else{
            List<String> words = getwordsbyp(map);
            if(words.size()<KEY_NUM){
                Searcher.Search("","",1,c);
            }else {
                for (int i = 0; i < KEY_NUM; i++) {
                    String word = words.get(i);
                    Searcher.RecSearch(word);
                }
            }
        }
    }

    public static List<String> getwordsbyp(Map map){
        double all = 0;
        for (Object o: map.keySet()){
            all+=(double)map.get(o);
        }
        List<Double> k = new ArrayList<Double>();
        for(int i=0;i<KEY_NUM;i++) {
            k.add(Math.random() * all);
        }
        Collections.sort(k);
        double now = 0;
        int index = 0;
        List<String> newList = new ArrayList<String>();
        for (Object o: map.keySet()){
            now+=(double)map.get(o);
            if(now>=k.get(index)){
                newList.add((String)o);
                if(++index==KEY_NUM){
                    break;
                }
            }
        }
        return newList;
    }



    /*public static List<Article> getnextRecommend(List<String> blockList,Context context){
        HashMap map = LoadSaver.LoadMap("map",context);
        List<Article> articalList = new ArrayList<Article>();
        int page = NOW_PAGE;
        System.out.print("page before:"+NOW_PAGE);
        Searcher.Search("",page,blockList);
        while(articalList.size()<10 && page-NOW_PAGE<=20){

            page++;
            List<Article> finalList = new ArrayList<Article>();
            if(newlist.isEmpty()){
                break;
            }
            for(int i=0;i<newlist.size();i++) {
                List<Keyword> keywordList = newlist.get(i).getKeywordList();
                double score = Scorer.getScore(keywordList,map);
                if(score>RECOMMEND_SCORE){
                    finalList.add(newlist.get(i));
                    //System.out.println(newlist.get(i).getTitle());
                }
            }
            //System.out.println(articalList.size());
            articalList.addAll(finalList);
        }
        NOW_PAGE = page;

        System.out.print("page after:"+NOW_PAGE);
        return articalList;
    }
    public static List<Article> getlatestRecommend(List<String> blockList,Context context,Callback yourcall){
        List<Article> articalList = new ArrayList<Article>();
        HashMap map = LoadSaver.LoadMap("map",context);
        int page = 1;
        while(articalList.size()<10 && page-NOW_PAGE<=20){
            Searcher.Search(""," ",page);
            page++;
            List<Article> finalList = new ArrayList<Article>();
            if(newlist.isEmpty()){
                break;
            }
            for(int i=0;i<newlist.size();i++) {
                List<Keyword> keywordList = newlist.get(i).getKeywordList();
                double score = Scorer.getScore(keywordList,map);
                if(score>RECOMMEND_SCORE){
                    finalList.add(newlist.get(i));
                    //System.out.print(score+" ");
                }
            }
            //System.out.print("-----------------\n");
            //System.out.println(articalList.size()+" "+finalList.size());
            articalList.addAll(finalList);
        }
        NOW_PAGE = page;
        return articalList;
    }*/
}