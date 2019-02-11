package com.weibo.ml.antispam.lsh;

import java.util.List;

public class RandomBinaryProjectionPlus extends LSHash{

    private int count;
    private int tables ;
    private int randomSeed ;

    public RandomBinaryProjectionPlus(String hashName, int tables, int count, int randomSeed){
        super(hashName);
        this.tables = tables;
        this.count = count;
        this.randomSeed = randomSeed;
    }

    @Override
    public void reset(int dim) {

    }

    @Override
    public List<String> hashVector(float[] v) {
        return null;
    }

    @Override
    public LSHashConfig getConfig() {
        return null;
    }

    @Override
    public void applyConfig(LSHashConfig config) {

    }
}
