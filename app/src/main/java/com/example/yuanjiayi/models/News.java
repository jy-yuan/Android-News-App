package com.example.yuanjiayi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {
    @SerializedName("pageSize")
    @Expose
    private String pageSize;

    @SerializedName("total")
    @Expose
    private int total;

    @SerializedName("data")
    @Expose
    private List<Article> articleList;

    public News(){

    }
    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Article> getArticle() {
        return articleList;
    }

    public void setArtileList(List<Article> dataList) {
        this.articleList = articleList;
    }
}
