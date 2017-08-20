package com.nowcoder.service;

import com.nowcoder.dao.MessageDao;
import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;

    public int addMessage(Message message){
       return  messageDao.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return  messageDao.getConversationDetail(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId, int offset,int limit){
        return messageDao.getConversationList(userId,offset,limit);
    }

    public int getConversationCount(int userId, String conversationId){
        return messageDao.getConversationCount(userId,conversationId);
    }

    public void updateStatus(int id){
        messageDao.updateStatus(id);
    }
}
