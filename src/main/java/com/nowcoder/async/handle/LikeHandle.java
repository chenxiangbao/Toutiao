package com.nowcoder.async.handle;

import com.nowcoder.async.EventHandle;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Component
public class LikeHandle implements EventHandle {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void deHandle(EventModel model) {
        Message message = new Message();
        //message.setToId(model.getEntityOwnerId());
        message.setToId(118);
        User user = userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的咨询" +model.getEntityId());
        message.setCreateDate(new Date());
        message.setFromId(119);
        String converstionId = message.getFromId()>message.getToId() ? message.getToId()+"_"+message.getFromId():message.getFromId()+"_"+message.getToId();
       message.setConversationId(converstionId);
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
