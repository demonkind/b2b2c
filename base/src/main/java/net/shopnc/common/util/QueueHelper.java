package net.shopnc.common.util;

import net.shopnc.b2b2c.config.ShopConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Queue<br/>
 * Created by dqw on 2016/02/01.
 */
@Component
public class QueueHelper {

    @Autowired
    private JedisPool jedisPool;

    /**
     * Queue前缀
     */
    private String prefix = "ncQueue_";

    /**
     * 读取队列
     * @return
     */
    public String pop() {
        if (!ShopConfig.getCacheOpen()) {
            return "";
        }
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lpop(prefix + "message");
        } finally {
            jedisPool.returnResourceObject(jedis);
        }
    }

    /**
     * 写入队列
     * @param value
     */
    public void push(String value) {
        if (!ShopConfig.getCacheOpen()) {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.rpush(prefix + "message", value);
        } finally {
            jedisPool.returnResourceObject(jedis);
        }
    }
}


