package com.fengwenyi.ssmshiroweb.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author Wenyi Feng
 * @since 2018-10-20
 */
@Component
public class JedisUtil {

    @Resource
    private JedisPool jedisPool;

    private Jedis getResource() {
        return jedisPool.getResource();
    }

    /**
     * 将值存到Redis中
     * @param key 唯一标识
     * @param value 二进制格式数据
     * @return 返回二进制格式数据，表示操作成功，反之，操作失败
     */
    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
            return value;
        } finally {
            jedis.close();
        }
    }

    /**
     * 设置过期时间
     * @param key 唯一标识
     * @param i 时间，单位：秒
     */
    public void expire(byte[] key, int i) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key, i); // 秒
        } finally {
            jedis.close();
        }
    }

    /**
     * 通过一个key获取value
     * @param key 唯一标识
     * @return 二进制格式数据
     */
    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * 通过一个key删除记录
     * @param key 唯一标识
     */
    public void delete(byte[] key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * 通过key前缀获取一个Set集合
     * @param shiro_session_prefix 前缀
     * @return 一个二进制的Set集合
     */
    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = getResource();
        try {
            return jedis.keys((shiro_session_prefix + "*").getBytes());
        } finally {
            jedis.close();
        }
    }
}
