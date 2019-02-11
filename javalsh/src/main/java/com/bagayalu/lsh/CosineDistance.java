package com.weibo.ml.antispam.lsh;

import com.weibo.ml.antispam.word2vec.Vector;

public class CosineDistance implements Distance {
    @Override
    public float distance(float[] x, float[] y) {
        return new Vector(x).cosine(new Vector(y));
    }
}
