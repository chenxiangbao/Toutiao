package com.nowcoder.controller;

import com.nowcoder.model.HostLocal;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;
    @Autowired
    HostLocal hostLocal;
    @Autowired
    UserService userService;


    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.POST,RequestMethod.GET})
    public String getConversationList(Model model){
        try {
            if(hostLocal.getUsers()==null){
                int userId = 3;
            }
            int userId = hostLocal.getUsers().getId();
            List<Message> messageList = messageService.getConversationList(userId,0,5);
            List<ViewObject> conversations = new ArrayList<>();
            for(Message message:messageList){
                ViewObject vo = new ViewObject();
                vo.set("conversation",message);
                int targetId = message.getFromId() == userId ? message.getToId():message.getFromId();
                vo.set("headUrl",userService.getUser(targetId).getHeadUrl());
                vo.set("unreadCount",messageService.getConversationCount(userId,message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            e.getMessage();
        }
        return "letter";
    }
    //如果需要分页。在页面做个前端分页按钮，触发事件。提交分页的页码以及每页的个数。就可以实现分页了。
    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.POST,RequestMethod.GET})
    public String getConversationDetail(Model model, @RequestParam("conversationId") String conversationId){
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<>();
            for(Message message:messageList){
                messageService.updateStatus(message.getId());
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                User user = userService.getUser(message.getFromId());
                if(user==null){
                    user = userService.getUser(3);
                }
                vo.set("headUrl",user.getHeadUrl());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            e.getMessage();
        }
        return "letterDetail";
    }
}
