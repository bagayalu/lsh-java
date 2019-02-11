package com.weibo.ml.antispam.lsh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBinaryProjections extends LSHash {

    private int count;
    private double[][] normals ;
    private Random rand;
    private int dim = 0;

    public RandomBinaryProjections(String hashName, int count, int randomSeed) {
        super(hashName);
        this.count = count;
        this.rand = new Random(randomSeed);
    }

    public RandomBinaryProjections(){
        this("default", 10, 2);
    }

    @Override
    public void reset(int dim) {
        if(this.dim == 0){
            this.dim = dim;
            this.normals = new double[count][this.dim];

            for(int i=0; i < count;i++){
                for(int j=0;j<dim;j++){
                    normals[i][j] = rand.nextGaussian();
                }
            }
        }
    }

    @Override
    public List<String> hashVector(float[] v) {
        List<String> keys = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.count;i++){
            double temp = 0.0;
            for(int j=0; j<this.dim; j++){
                temp+= normals[i][j]*v[i];
            }
            sb.append(temp > 0.0 ? '1':'0');
        }
        keys.add(sb.toString());
        return keys;
    }

    @Override
    public LSHashConfig getConfig() {
        LSHashConfig conf = new LSHashConfig();
        conf.setDim(this.dim);
        conf.setHashName(this.hashName);
        conf.setNormals(this.normals);
        conf.setProjectionCount(this.count);
        return conf;
    }

    @Override
    public void applyConfig(LSHashConfig conf) {
        this.hashName = conf.getHashName();
        this.dim = conf.getDim();
        this.count = conf.getProjectionCount();
        this.normals = conf.getNormals();
    }
}
