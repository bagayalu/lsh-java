package com.weibo.ml.antispam.lsh;

import com.weibo.ml.antispam.word2vec.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Engine {

    public static Logger _LOG = LoggerFactory.getLogger(Engine.class);
    private LSHash lshash;

    private Distance distance;

    private VectorFilter vectorFilter;

    private Storage storage;


    public Engine(int dim, LSHash lshash, Distance distance, VectorFilter vectorFilter, Storage storage){
        _LOG.info("start initialize lsh engine...");

        if(lshash == null){
            this.lshash = new RandomBinaryProjections("default", 10,1);
        }else{
            this.lshash = lshash;
        }
        if(distance == null){
            this.distance = new CosineDistance();
        }else{
            this.distance = distance;
        }
        if(vectorFilter == null){
            this.vectorFilter = new NearestFilter(10);
        }else
            this.vectorFilter= vectorFilter;
        if(storage == null){
//            this.storage = new StorageRedis();
        }else{
            this.storage = storage;
        }
        this.lshash.reset(dim);
    }

    public boolean storeVector(Vector v , String data){
        for(String key : this.lshash.hashVector(v.getElementArray())){
            this.storage.storeVector(this.lshash.hashName, key, v.getElementArray(), data);
        }
        return true;
    }

    public boolean deleteVector(Vector v, String data){
        List<String> keys = lshash.hashVector(v.getElementArray());
        this.storage.deleteVector(lshash.hashName,keys);

        return true;
    }

    public int candidateCount(float[] v){
        return this.getCandidates(v).size();
    }

    private ArrayList<Candidate> getCandidates(float[] v){
        Set<Candidate> candidates = new HashSet<>();
        List<String> keys = this.lshash.hashVector(v);
        for(String key : keys){
            candidates.addAll(this.storage.getBucket(lshash.hashName, key));
        }
        return new ArrayList<>(candidates);
    }

    /**
     *
     * @param v
     * @return
     */
    public ArrayList<Candidate> neighbours(float[] v){
        ArrayList<Candidate> candidates = this.getCandidates(v);

        System.out.println(candidates.size());
        if(this.distance != null){
            candidates = this.appendDistance(v, candidates);
        }

        if(this.vectorFilter !=null ){
            candidates = this.applyFilters(candidates);
        }

        return  candidates;

    }

    private ArrayList<Candidate> applyFilters(ArrayList<Candidate> candidates) {
        return this.vectorFilter.filterVectors(candidates);

    }

    private ArrayList<Candidate> appendDistance(float[] v, ArrayList<Candidate> candidates){
        for (Candidate candidate : candidates) {
            candidate.setSimilarity(this.distance.distance(v, candidate.getVector()));
        }
        return candidates;
    }

    public boolean cleanAllBucket(){
        this.storage.cleanAllBucket();
        return true;
    }

    public ArrayList<String> getAllBucket(){
        ArrayList<String> keys = this.storage.getAllBucketKeys(this.lshash.hashName);
        ArrayList<String> res = new ArrayList<>();
        int count = 0;
        for (String key : keys) {
            List<Candidate> texts = this.storage.getBucket(key);
            count += texts.size();
            if(texts.size() > 14){
                res.add(key);
                for (Candidate text : texts) {
                    res.add(text.data);
                }
            }
        }
        System.out.println(count);
        return res;
    }
}
