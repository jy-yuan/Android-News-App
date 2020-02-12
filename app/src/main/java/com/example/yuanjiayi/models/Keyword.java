package com.example.yuanjiayi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Keyword {
    @SerializedName("score")
    @Expose
    private double score;

    @SerializedName("word")
    @Expose
    private String word;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
