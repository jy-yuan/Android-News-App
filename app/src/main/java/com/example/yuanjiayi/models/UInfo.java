package com.example.yuanjiayi.models;

import java.util.HashMap;
import java.util.List;

public class UInfo {
    public String name;
    public String psw;
    HashMap<String,String> hismap;

    HashMap<String,Float> map;

    List<Article> newsList;

    List<Article> favList;

    Boolean categories[]  = new Boolean[10];

    List<String> BlockList;


    public UInfo(String _name,String _psw){
        name = _name;
        psw = _psw;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public HashMap<String, String> getHismap() {
        return hismap;
    }

    public void setHismap(HashMap<String, String> hismap) {
        this.hismap = hismap;
    }

    public HashMap<String, Float> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Float> map) {
        this.map = map;
    }

    public List<Article> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<Article> newsList) {
        this.newsList = newsList;
    }

    public List<Article> getFavList() {
        return favList;
    }

    public void setFavList(List<Article> favList) {
        this.favList = favList;
    }

    public Boolean[] getCategories() {
        return categories;
    }

    public void setCategories(Boolean[] categories) {
        this.categories = categories;
    }

    public List<String> getBlockList() {
        return BlockList;
    }

    public void setBlockList(List<String> blockList) {
        BlockList = blockList;
    }
}
