package com.weibo.ml.antispam.lsh;

import com.weibo.ml.antispam.utils.ProtostuffUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StorageRedis implements  Storage{

    private JedisPool jedisPool;
    private int redisDb ;
    private int timeInSeconds = 86400;
    public static Logger _LOG = LoggerFactory.getLogger(StorageRedis.class);

    public StorageRedis(String redisHost, int redisPort, int redisDB, int timeInSeconds) {
        this.jedisPool = new JedisPool(redisHost, redisPort);
        this.timeInSeconds = timeInSeconds;
        this.redisDb = redisDB;
    }

    @Override
    public boolean storeVector(String hashName, String bucketKey, float[] v, String data) {
        String redisKey = this.formatRedisKey(hashName, bucketKey);
        //this place need to compress the sparse v
        Candidate candidate = new Candidate(v, data);

//        System.out.println(redisKey);
        byte[] serializerResult = ProtostuffUtils.serialize(candidate);

        try(Jedis jedis = jedisPool.getResource()){
            jedis.rpush(redisKey.getBytes(),serializerResult);
            jedis.expire(redisKey, timeInSeconds);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            _LOG.error(e.getMessage());
            return false;
        }
    }

    private String formatRedisKey(String hashName, String bucketKey){
        return String.format("%s_%s", this.formatHashPrefix(hashName), bucketKey);
    }

    private String formatHashPrefix(String hashName){
        return String.format("nearjava_%s", hashName);
    }

    @Override
    public boolean deleteVector(String hashName, List<String> bucketKeys) {
        ArrayList<String> keys = new ArrayList<>();
        for(String key : bucketKeys){
            keys.add(this.formatRedisKey(hashName, key));
        }
        try(Jedis jedis = this.jedisPool.getResource()){
            jedis.del(keys.toArray(new String[0]));
            return true;

        }catch (Exception e){
            e.printStackTrace();
            _LOG.error(e.getMessage());
            return false;
        }
    }

    /**
     *
     * @param hashName
     * @param bucketKey
     * @return
     */
    @Override
    public List<Candidate> getBucket(String hashName, String bucketKey) {
        String redisKey = this.formatRedisKey(hashName,bucketKey);
        System.out.println(redisKey);
        return this.getBucket(redisKey);
    }


    public List<Candidate> getBucket(String redisKey){
        try(Jedis jedis = jedisPool.getResource()){
            List<byte[]> resBytes = jedis.lrange(redisKey.getBytes(), 0, -1);
            List<Candidate> res = new ArrayList<>();
            for (byte[] re : resBytes) {
                res.add(ProtostuffUtils.deserialize(re, Candidate.class));
            }
            return res;
        }catch (Exception e){
            e.printStackTrace();
            _LOG.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取所有key,由于jedis scan方法不再推荐使用，考虑用pipline做
     * @param hashName
     * @return
     */
    @Override
    public ArrayList<String> getAllBucketKeys(String hashName) {
        String pattern = String.format("%s*", this.formatHashPrefix(hashName));
        try(Jedis jedis = jedisPool.getResource()){
            Set<String> ks = jedis.keys(pattern);
            ArrayList res = new ArrayList();
            res.addAll(ks);
            return res;
        }catch (Exception e){
            e.printStackTrace();
            _LOG.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean cleanBucket(String hashName) {
        try(Jedis jedis = jedisPool.getResource()){
            Set<String> keys = jedis.keys(hashName + "*");
            jedis.del(keys.toArray(new String[0]));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            _LOG.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean cleanAllBucket() {
        try(Jedis jedis = jedisPool.getResource()){
            jedis.flushDB();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            _LOG.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean storeHashConf(LSHash lsHash) {
        LSHashConfig conf = lsHash.getConfig();
        byte[] serializerResult = ProtostuffUtils.serialize(conf);
        String confKey = lsHash.hashName + "_conf";
        try(Jedis jedis = jedisPool.getResource()){
            jedis.set(confKey.getBytes(), serializerResult);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public LSHashConfig loadHashConf(String hashName) {
        String confKey = hashName + "_conf";
        try(Jedis jedis = jedisPool.getResource()){
            byte[] bconf = jedis.get(confKey.getBytes());
            if(bconf == null || bconf.length == 0)
                return null;
            LSHashConfig conf = ProtostuffUtils.deserialize(bconf, LSHashConfig.class);
            return conf;
        }catch(Exception e){
            e.printStackTrace();
            _LOG.error(e.getMessage());
            return null;
        }
    }
}
