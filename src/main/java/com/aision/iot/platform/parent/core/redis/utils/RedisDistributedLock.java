package com.aision.iot.platform.parent.core.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yim
 * @description redis分布式锁
 * @date 2019/5/8
 */
@Component
public class RedisDistributedLock {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * redis分布式锁-上锁
     * @param key 锁名
     * @param acquireTimeout 重试获取锁时间
     * @param timeout 锁时间
     * @return 锁内容， 为null则上锁失败
     */
    public String lockWithTimeout(String key, long acquireTimeout, long timeout){
        RedisConnectionFactory factory = stringRedisTemplate.getConnectionFactory();
        RedisConnection redisConnection = factory.getConnection();
        long end = System.currentTimeMillis() + acquireTimeout;
        int lockExpire = (int)timeout/1000;
        while (System.currentTimeMillis() < end){
            byte[] bytes = redisConnection.get(key.getBytes());
            String lockTime = new String(bytes);
            if (redisConnection.setNX(key.getBytes(), String.valueOf(System.currentTimeMillis()).getBytes())){
                redisConnection.expire(key.getBytes(), lockExpire);
                RedisConnectionUtils.releaseConnection(redisConnection, factory);
                return lockTime;
            }
            if (redisConnection.ttl(key.getBytes()) == -1) {
                redisConnection.expire(key.getBytes(), lockExpire);
            }
        }
        RedisConnectionUtils.releaseConnection(redisConnection, factory);
        return null;
    }

    /**
     * redis分布式锁-解锁
     * @param key 锁名
     * @param value 锁内容
     * @return 解锁结果true 或者false
     */
    public boolean realseLock(String key, String value){
        boolean flag = false;
        RedisConnectionFactory factory = stringRedisTemplate.getConnectionFactory();
        RedisConnection redisConnection = factory.getConnection();
        while (true){
            redisConnection.watch(key.getBytes());
            byte[] bytes = redisConnection.get(key.getBytes());
            if (bytes == null){
                redisConnection.unwatch();
                flag = true;
                break;
            }
            String oldVaule = new String(bytes);
            if (oldVaule.equals(value)){
                redisConnection.multi();
                redisConnection.del(key.getBytes());
                List<Object> list = redisConnection.exec();
                if (list == null){
                    continue;
                }
                flag = true;
            }
            redisConnection.unwatch();
            break;
        }
        RedisConnectionUtils.releaseConnection(redisConnection, factory);
        return flag;
    }
}
