package com.aision.iot.platform.parent.core.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author yim redis工具使用类
 * @description IRedisService
 * @date 2019/3/18
 */
@Component
public class RedisService<T> {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;
    @Resource
    protected HashOperations<String, String , T> hashOperations;

    /**
     * 添加 String类型
     * @param key    key
     * @param value 对象
     */
    public void put(String key, Object value) {
        this.put(key, value, null);
    }

    /**
     * 添加 String类型
     * @param key    key
     * @param value 对象
     * @param expire 过期时间(单位:秒)
     */
    public void put(String key, Object value, Long expire) {
        redisTemplate.opsForValue().set(key, value);
        if (expire != null) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 删除 String类型
     *
     * @param key 传入key的名称
     */
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 查询 String类型
     *
     * @param key 查询的key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 查询指定类型的key
     * String类型
     * @return
     */
    public Set<String> getKeys(String pattern) {
       return redisTemplate.keys(pattern);
    }

    /**
     * 查询指定hashMap类型的key
     * @param pattern
     * @return
     */
    public Set<String> getHKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 判断key是否存在redis中
     * String类型
     * @param key 传入key的名称
     * @return
     */
    public boolean isKeyExists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     *
     * 添加至Hash类型数据
     * @param key    key
     * @param hashKey hashKey
     * @param value 对象
     */
    public void putHash(String key, String hashKey, T value) {
        this.putHash(key, hashKey, value, null);
    }
    /**
     * 添加至Hash类型数据
     * @param key key
     * @param hashKey hashKey
     * @param value 对象
     * @param expire 过期时间(单位:秒)
     */
    public void putHash(String key, String hashKey, T value, Long expire) {
        hashOperations.put(key, hashKey, value);
        if (expire != null) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 删除Hash类型中的数据
     * @param key 传入key的名称
     * @param hashKey hashKey
     */
    public void removeHash(String key, String hashKey) {
        hashOperations.delete(key, hashKey);
    }

    /**
     * 查询Hash类型中的数据
     * @param key 查询的key
     * @param hashKey hashKey
     * @return
     */
    public T getHash(String key, String hashKey) {
        return hashOperations.get(key, hashKey);
    }
    /**
     * 获取当前Hash下所有对象
     * @param key
     * @return
     */
    public List<T> getHashAll(String key) {
        return hashOperations.values(key);
    }

    /**
     * 查询当前hash下所有key
     *@param key
     * @return
     */
    public Set<String> getHashKeys(String key) {
        return hashOperations.keys(key);
    }

    /**
     * 判断key是否存在hash中
     *
     * @param key 传入key的名称
     * @param hashKey 传入hashKey的名称
     * @return
     */
    public boolean isKeyExistsHash(String key, String hashKey) {
        return hashOperations.hasKey(key, hashKey);
    }

    /**
     * 查询当前Hash key下缓存数量
     * @param key 传入key的名称
     * @return
     */
    public long countHash(String key) {
        return hashOperations.size(key);
    }

    /**
     * 清空hash里面的数据
     * @param key 传入key的名称
     */
    public void emptyHash(String key) {
        Set<String> set = hashOperations.keys(key);
        set.stream().forEach(hashKey -> hashOperations.delete(key, hashKey));
    }


}
