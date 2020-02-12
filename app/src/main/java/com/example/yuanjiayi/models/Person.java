package com.example.yuanjiayi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("linkedURL")
    @Expose
    private String linkedURL;

    @SerializedName("mention")
    @Expose
    private String mention;

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

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }
}
