package com.weibo.ml.antispam.lsh;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 最近邻方法实现
 */
public class NearestFilter implements VectorFilter {

    private int N;

    public NearestFilter(int N){
        this.N = N;
    }

    @Override
    public ArrayList<Candidate> filterVectors(ArrayList<Candidate> list) {
        ArrayList<Candidate> tmpList = new ArrayList<>(list);
        Collections.sort(tmpList);
        if(tmpList.size() <= N){
            return tmpList;
        }else{
            return new ArrayList<>(tmpList.subList(0, tmpList.size()-1));
        }
    }
}
