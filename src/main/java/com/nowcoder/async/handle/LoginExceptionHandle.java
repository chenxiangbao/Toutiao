package com.nowcoder.async.handle;

import com.nowcoder.async.EventHandle;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoginExceptionHandle implements EventHandle{

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    MailSender mailSender;

    @Override
    public void deHandle(EventModel model) {
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次登录异常");
        message.setFromId(118);
        message.setCreateDate(new Date());
        String converstionId = message.getFromId()>message.getToId() ? message.getToId()+"_"+message.getFromId():message.getFromId()+"_"+message.getToId();
        message.setConversationId(converstionId);
        messageService.addMessage(message);
        Map<String ,Object> map = new HashMap<>();
        map.put("username",model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"),"登录异常","mails",map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
