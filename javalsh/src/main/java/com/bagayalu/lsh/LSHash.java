package com.weibo.ml.antispam.lsh;

import org.json.simple.JSONObject;

import java.util.List;

public abstract class LSHash {

    protected String hashName ;

    public LSHash(String hashName){

        this.hashName = hashName;

    }

    public LSHash(){

    }

    public abstract void reset(int dim);

    public abstract List<String> hashVector(float[] v);

    public abstract LSHashConfig getConfig();

    public abstract void applyConfig(LSHashConfig config);
}
