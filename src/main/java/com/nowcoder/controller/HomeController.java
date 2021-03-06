package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Controller
public class HomeController {
    @Autowired
    NewsService newsService;
    @Autowired
    HostLocal hostLocal;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
        int localHostId = hostLocal.getUsers() !=null ? hostLocal.getUsers().getId() :0;
        List<ViewObject> vos = new ArrayList<>();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            //调整赞的人数以及状态
            if(localHostId!=0){
                vo.set("like",likeService.getLikeStatus(localHostId, EntityType.ENTITY_NEWS,news.getId()));
            }else{
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        model.addAttribute("vos", getNews(0, 0, 10));

        return "home";
    }

    @RequestMapping(path = {"/user/{userId}/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId, @RequestParam(value = "pop",defaultValue = "0") int pop) {
        model.addAttribute("vos", getNews(userId, 0, 10));
        model.addAttribute("pop",pop);
        return "home";
    }


}
