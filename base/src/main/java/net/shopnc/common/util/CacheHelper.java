package net.shopnc.common.util;

import net.shopnc.b2b2c.config.ShopConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Cache<br/>
 * Created by dqw on 2016/01/27.
 */
@Component
public class CacheHelper {

    @Autowired
    private JedisPool jedisPool;

    /**
     * cache前缀
     */
    private String prefix = "ncCache_";

    /**
     * 根据key获取缓存数据
     * @param key
     * @return
     */
    public String get(String key) {
        if (!ShopConfig.getCacheOpen()) {
            return "";
        }
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.get(prefix + key);
        } finally {
            jedisPool.returnResourceObject(jedis);
        }
    }

    /**
     * 根据key设置缓存数据，如果以前存在更新，如果以前没有添加
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        if (!ShopConfig.getCacheOpen()) {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(prefix + key, value);
        } finally {
            jedisPool.returnResourceObject(jedis);
        }
    }

    /**
     * 根据key删除缓存数据
     * @param key
     */
    public void del(String key) {
        if (!ShopConfig.getCacheOpen()) {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(prefix + key);
        } finally {
            jedisPool.returnResourceObject(jedis);
        }
    }
}


