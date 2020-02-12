package com.example.yuanjiayi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("lng")
    @Expose
    private double lng;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("linkedURL")
    @Expose
    private String linkedURL;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("mention")
    @Expose
    private String mention;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLinkedURL() {
        return linkedURL;
    }

    public void setLinkedURL(String linkedURL) {
        this.linkedURL = linkedURL;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }
}
