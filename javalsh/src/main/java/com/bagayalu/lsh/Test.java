package com.bagayalu.lsh;

/**
 * Hello world!
 *
 */
public class Test {

    private Engine lshEngine;

    /**
     *
     * @param redisHost  redis host
     * @param redisPort  redis port
     * @param redisDB   redis database
     * @param dim
     * @param hashName
     * @param protectionCount
     * @param randomSeed
     * @param nearestCount
     * @param timeInseconds
     */
    private void initializeLsh(String redisHost, int redisPort, int redisDB,int dim, String hashName, int protectionCount, int randomSeed, int nearestCount, int timeInseconds) {
        Distance consine = new CosineDistance();
        VectorFilter vectorFilter = new NearestFilter(nearestCount);
        Storage storage = new StorageRedis(redisHost, redisPort, redisDB,timeInseconds);
        LSHashConfig conf = storage.loadHashConf(hashName);
        LSHash lshash;
        if(conf == null){
            _LOG.info("no lsh config found in redis ,create a new one");
            lshash = new RandomBinaryProjections(hashName,protectionCount, randomSeed);
            storage.storeHashConf(lshash);
        }else{
            _LOG.info("lsh config found in redis, apply it");
            lshash = new RandomBinaryProjections();
            lshash.applyConfig(conf);
        }
        this.lshEngine = new Engine(dim,lshash, consine,vectorFilter,storage);
    }

    public static void main(String[] args) {



    }
}
