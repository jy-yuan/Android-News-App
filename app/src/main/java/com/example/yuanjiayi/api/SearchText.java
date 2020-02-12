package com.example.yuanjiayi.api;

public class SearchText {
    private String words;
    private int size;
    private String category;
    private String startDate;
    private String endDate;

    public SearchText(String _words){
        words = _words;
        size = 15;
        category = "";
        startDate = "";
        endDate = "2020-01-01";
    }

    public SearchText(String _words,int _size){
        words = _words;
        size = _size;
        category = "";
        startDate = "";
        endDate = "2020-01-01";
    }

    public SearchText(String _words,int _size,String _category){
        words = _words;
        size = _size;
        category = _category;
        startDate = "";
        endDate = "2020-01-01";
    }
    public SearchText(String _words,int _size,String _category,String _startDate,String _endDate){
        words = _words;
        size = _size;
        category = _category;
        startDate = _startDate;
        endDate = _endDate;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}



