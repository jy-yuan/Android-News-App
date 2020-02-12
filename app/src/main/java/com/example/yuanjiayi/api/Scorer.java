package com.example.yuanjiayi.api;
import com.example.yuanjiayi.models.*;
import java.util.*;

public class Scorer {
    public Scorer(){

    }
    public static double getScore(List<Keyword> keywordList,Map<String,Double> map){
        double score = 0;
        double all = 0;
        if(keywordList.size()==0){
            return 0;
        }
        for(int i=0;i<keywordList.size();i++){
            if(map.containsKey(keywordList.get(i).getWord())) {
                score += keywordList.get(i).getScore() * (double)map.get(keywordList.get(i).getWord());
            }
        }
        for (Object o: map.keySet()){
            all+=(double)map.get(o);
        }
        if(all==0){
            return 1;
        }
        return score/all;
    }
}