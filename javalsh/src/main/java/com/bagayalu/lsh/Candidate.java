package com.weibo.ml.antispam.lsh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Candidate implements Serializable,Comparable<Candidate>{

    String key;
    String data;
    float similarity;
    float[] vector;

    public Candidate(String data, float similarity){
        this.data = data;
        this.similarity = similarity;
    }

    public Candidate(float[] v, String data){
        this.data = data;
        this.vector = v;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float[] getVector() {
        return vector;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }

    @Override
    public int compareTo( Candidate o) {
        int res = 0;
        res = (this.similarity - o.similarity) >0 ? 1:-1;
        return res;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "data='" + data + '\'' +
                ", similarity=" + similarity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(data, candidate.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    public static void main(String[] args) {
        ArrayList<Candidate> list = new ArrayList<>();
        list.add(new Candidate("ccc", 3.0f));
        list.add(new Candidate("aaa", 5.0f));
        list.add(new Candidate("bbb", 2.0f));
        list.add(new Candidate("ddd", 4.0f));
        Collections.sort(list);
        System.out.println(list.toString());
    }
}
