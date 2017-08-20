package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProduce;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostLocal;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.util.TouTiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    HostLocal hostLocal;
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;
    @Autowired
    EventProduce eventProduce;

    @RequestMapping("/like")
    @ResponseBody
    public String like( @RequestParam("newsId") int newsId){
        int userId = hostLocal.getUsers() !=null ? hostLocal.getUsers().getId() :0;
        long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        //入队列
        eventProduce.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostLocal.getUsers().getId()).setEntityId(newsId).setEntityType(EntityType.ENTITY_NEWS)
        .setEntityOwnerId(newsService.getByNewsId(newsId).getUserId()));

        return TouTiaoUtil.getJsonString(0,String.valueOf(likeCount));
    }

    @RequestMapping("/dislike")
    @ResponseBody
    public String disLike( @RequestParam("newsId") int newsId){
        int userId = hostLocal.getUsers() !=null ? hostLocal.getUsers().getId() :0;
        long likeCount = likeService.disLike(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        return TouTiaoUtil.getJsonString(0,String.valueOf(likeCount));
    }
}

