package com.fh.util;

import com.fh.model.User;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class RedisUtil {
    public static String get(String key){
        Jedis resources =null;
        String getKey =null;
        try {
             resources = RedisPool.getResources();
             getKey = resources.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException(e.getMessage());
        } finally {
            if(resources!=null){
                resources.close();
            }
        }
        return getKey;
    }
    public static void set(String key, List value){
        Jedis resources =null;
        try {
            resources = RedisPool.getResources();
            resources.set(key, String.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if(resources!=null){
                resources.close();
            }
        }
    }

    public static void set(String key, String value){
        Jedis resources =null;
        try {
            resources = RedisPool.getResources();
            resources.set(key, String.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if(resources!=null){
                resources.close();
            }
        }
    }

    public static void del(String key){
        Jedis resources =null;
        try {
            resources= RedisPool.getResources();
            resources.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if(resources!=null){
                resources.close();
            }
        }
    }

    public static void expire(String key,int seconds){
        Jedis resources =null;
        try {
            resources=RedisPool.getResources();
            resources.expire(key,seconds);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if(resources!=null){
                resources.close();
            }
        }
    }

    public static void setex(String key,int seconds,String value){
        Jedis resources =null;
        try {
            resources= RedisPool.getResources();
            resources.setex(key,seconds,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if(resources!=null){
                resources.close();
            }
        }
    }

    @Test
    public void test(){
        List list = new ArrayList();
        list.add(1);
        list.add("张");
        RedisUtil.set("list",list);
    }

    @Test
    public void testget(){
        String a = RedisUtil.get("list");
        System.out.println(a);
    }
}
