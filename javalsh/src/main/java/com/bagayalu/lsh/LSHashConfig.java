package com.weibo.ml.antispam.lsh;

import java.io.Serializable;

public class LSHashConfig implements Serializable {
    private int dim;
    private String hashName;
    private double[][] normals;
    private int projectionCount;

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    public String getHashName() {
        return hashName;
    }

    public void setHashName(String hashName) {
        this.hashName = hashName;
    }

    public double[][] getNormals() {
        return normals;
    }

    public void setNormals(double[][] normals) {
        this.normals = normals;
    }

    public int getProjectionCount() {
        return projectionCount;
    }

    public void setProjectionCount(int projectionCount) {
        this.projectionCount = projectionCount;
    }
}
