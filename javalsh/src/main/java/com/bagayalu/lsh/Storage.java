package com.weibo.ml.antispam.lsh;

import java.util.ArrayList;
import java.util.List;

public interface Storage {

     boolean storeVector(String hashName, String bucketKey, float[] v, String data);

     ArrayList<String> getAllBucketKeys(String hashName);

     boolean deleteVector(String hashName, List<String> bucketKeys);

     List<Candidate> getBucket(String hashName, String bucketKey);

     List<Candidate> getBucket(String redisKey);

     boolean cleanBucket(String hashName);

     boolean cleanAllBucket();

     boolean storeHashConf(LSHash lsHash);

     LSHashConfig loadHashConf(String hashName);

}
