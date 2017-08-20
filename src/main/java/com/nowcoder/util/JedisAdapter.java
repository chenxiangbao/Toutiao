package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;


@Service
public class JedisAdapter implements InitializingBean{
    private static Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private  JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("192.168.1.108",6379);
    }

    private Jedis getJedis(){
        return pool.getResource();
    }

   //将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
    public  long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    //移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
    public  long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    //判断 member 元素是否集合 key 的成员。
    public Boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    //返回集合 key 的基数(集合中元素的数量)。
    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    //将一个或多个值 value 插入到列表 key 的表头
    public long lpush(String key,String value){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    //BRPOP 是列表的阻塞式(blocking)弹出原语。
    //它是 RPOP 命令的阻塞版本，
    // 当给定列表内没有任何元素可供弹出的时候，连接将被 BRPOP 命令阻塞，直到等待超时或发现可弹出元素为止。
    public List brpop(int timeout,String key){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            return jedis.brpop(timeout,key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public void setObject(String key,Object obj){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            jedis.set(key, JSON.toJSONString(obj));
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public <T>T getObject(String key,Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            String value= jedis.get(key);
            if(value!=null){
                return JSON.parseObject(value,clazz);
            }
            return null;
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
}
