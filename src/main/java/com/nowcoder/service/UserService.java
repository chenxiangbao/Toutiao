package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDao;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.TouTiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDao loginTicketDao;

    User user =null;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    /**
     * 注册
     * @param username
     * @param password
     * @param regist
     * @return
     */
    public Map<String,Object> regist(String username, String password){
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){
            map.put("magname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgname","密码不能为空");
            return map;
        }
        user = userDAO.selectByName(username);
        if(user!=null ){
            map.put("msgname","用户名已存在");
            return map;
        }
        user = new User();
        user.setName(username);
        //密码加密
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(TouTiaoUtil.getMD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登录

        return map;
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public Map<String,Object> login(String username,String password){
        Map<String,Object> map = new HashMap<String,Object>();
        User user = userDAO.selectByName(username);
        if(user==null){
            map.put("msg","用户名或者密码错误！");
            return map;
        }
        if(!TouTiaoUtil.getMD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","用户名或者密码错误！");
            return map;
        }
        //ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    /**
     * 令牌
     * @param userId
     * @return
     */
    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setStatus(0);
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*24);
        loginTicket.setExpired(date);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));

        loginTicketDao.addLoginTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void annUser(User user){
        userDAO.addUser(user);
    }
    public User getUser(String username){
        if(username!=null){
            user = userDAO.selectByName(username);
            return user;
        }
        return null;
    }
    public void loginOut(String ticket){
        loginTicketDao.updateTicketStatus(ticket,1);
    }
}
