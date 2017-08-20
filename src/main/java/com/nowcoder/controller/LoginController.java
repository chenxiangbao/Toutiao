package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProduce;
import com.nowcoder.async.EventType;
import com.nowcoder.model.HostLocal;
import com.nowcoder.service.UserService;
import com.nowcoder.util.TouTiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    EventProduce eventProduce;

    @RequestMapping(path = {"/reg/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String regist(Model model, @RequestParam("username") String username, @RequestParam("password") String password,
                         @RequestParam(value = "rember",defaultValue = "0") String rember){
        try {
            Map<String,Object> map = userService.regist(username,password);
            if(map.isEmpty()){
                return TouTiaoUtil.getJsonString(0,map);
            }else{
                return TouTiaoUtil.getJsonString(1,map);
            }

        }catch (Exception e){
            return TouTiaoUtil.getJsonString(1,"注册异常");
        }
    }
    @RequestMapping(path = {"/login/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username, @RequestParam("password") String password,
                        @RequestParam(value = "rember",defaultValue = "0") String rember, HttpServletResponse response){
        try {
            Map<String,Object> map = userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                //如果注册异常，加入队列，进行站内信发送处理
                //此处可能有问题 map.get("userId")
                eventProduce.fireEvent(new EventModel(EventType.LOGIN).
                        setActorId(userService.getUser(username).getId()).
                        setExt("username",username).setExt("email","zjuyxy@qq.com"));
                return TouTiaoUtil.getJsonString(0,"登录成功");
            }else{
                return TouTiaoUtil.getJsonString(1,map);
            }
        }catch (Exception e){
            return TouTiaoUtil.getJsonString(1,"用户名或者密码错误");
        }
    }
    @RequestMapping(path = {"/loginout/"},method = {RequestMethod.GET,RequestMethod.POST})
    public String loginOut(@CookieValue("ticket")String ticket){
        userService.loginOut(ticket);
        return "redirect:/";
    }
}
