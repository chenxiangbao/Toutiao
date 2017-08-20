package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean ,ApplicationContextAware{
    private static Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType,List<EventHandle>> config = new HashMap<EventType,List<EventHandle>>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    //初始化操作
    @Override
    public void afterPropertiesSet() throws Exception {
        //找出所有实现EventHandle接口的类
        Map<String,EventHandle> beans = applicationContext.getBeansOfType(EventHandle.class);
        if(beans!=null){
           for (Map.Entry<String,EventHandle> entry: beans.entrySet()){
               //遍历所有实现EventHandle接口的type，例如likeHandle,其type就是like
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for(EventType type :eventTypes){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandle>());
                    }
                    config.get(type).add(entry.getValue());
                }
           }
        }
        //线程主要是取出进入队列的事件。然后通知config中绑定的handle进行事件的处理

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events =jedisAdapter.brpop(0,key);
                    for(String message :events){
                        if(message.equals(key)){
                            continue;
                        }
                        //存放的时候的转换成String的json存入的.取出来进行转换成对象
                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
                        //如果config中绑定这个事件。
                        if(!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件");
                            continue;
                        }
                        //当前type对应的list集合。Map<EventType,List<EventHandle>>，
                        // 轮流执行，不需要知道是那个实现类。只要是实现接口的实现类都给他进行传参。
                        for(EventHandle handle:config.get(eventModel.getType())){
                            //进行处理
                            handle.deHandle(eventModel);
                        }
                    }

                }
            }
        });
        thread.start();
    }


    //记录applicationContext
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
