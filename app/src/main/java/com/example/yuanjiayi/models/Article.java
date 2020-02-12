package com.example.yuanjiayi.models;

import java.util.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {
    @SerializedName("image")
    @Expose
    private String image;

    private String imagetourl;

    @SerializedName("publishTime")
    @Expose
    private String time;

    @SerializedName("keywords")
    @Expose
    private List<Keyword> keywordList;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("video")
    @Expose
    private String video;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("when")
    @Expose
    private List<Keyword> whenList;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("persons")
    @Expose
    private List<Person> personList;

    @SerializedName("newsID")
    @Expose
    private String newsID;

    @SerializedName("crawlTime")
    @Expose
    private String crawlTime;

    @SerializedName("organizations")
    @Expose
    private List<Person> organizationList;

    @SerializedName("publisher")
    @Expose
    private String publisher;

    @SerializedName("locations")
    @Expose
    private List<Location> locationList;

    @SerializedName("where")
    @Expose
    private List<Keyword> whereList;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("who")
    @Expose
    private List<Keyword> whoList;

    private STATE state;

    public enum STATE{
        NEW,
        HIS,
        FAV
    }
    public STATE getState() {
        return state;
    }
    public String getstrofs(){
        switch (state) {
            case NEW:
                return "NEW";
            case HIS:
                return "HIS";
            case FAV:
                return "FAV";
        }
        return "NEW";
    }

    public void setState(String state) {
        switch (state){
            case "NEW":
                this.state = STATE.NEW;
                break;
            case "HIS":
                this.state = STATE.HIS;
                break;
            case "FAV":
                this.state = STATE.FAV;
                break;
        }
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Keyword> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<Keyword> keywordList) {
        this.keywordList = keywordList;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Keyword> getWhenList() {
        return whenList;
    }

    public void setWhenList(List<Keyword> whenList) {
        this.whenList = whenList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Person> getPersonList() {
        if(personList == null || personList.isEmpty()){
            personList = new ArrayList<Person>();
            personList.add(new Person());
        }
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public String getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(String crawlTime) {
        this.crawlTime = crawlTime;
    }

    public List<Person> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Person> organizationList) {
        this.organizationList = organizationList;
    }

    public String getPublishedAt() {
        return publisher;
    }

    public void setPublishedAt(String publisher) {
        this.publisher = publisher;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public List<Keyword> getWhereList() {
        return whereList;
    }

    public void setWhereList(List<Keyword> whereList) {
        this.whereList = whereList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Keyword> getWhoList() {
        return whoList;
    }

    public void setWhoList(List<Keyword> whoList) {
        this.whoList = whoList;
    }

    public List<String> getUrlToImage() {
        List<String> list;
        if(image.length() < 5){
            list = new ArrayList<>();
            list.add(" ");
        } else {
            list = Arrays.asList(image.substring(1, image.length() - 1).split(", "));
        }
        return list;
    }

    public void setUrlToImage(String imagetourl) {
        this.imagetourl = imagetourl;
    }

    public List<String> getKeys(){
        List<String> keylist = new ArrayList<String>();
        for(int i=0;i<keywordList.size();i++){
            keylist.add(keywordList.get(i).getWord());
        }
        return keylist;
    }

    @Override
    public boolean equals(Object obj) {
        return this.newsID==((Article)obj).getNewsID();
    }

    @Override
    public int hashCode() {
        return this.newsID.hashCode();
    }
}
