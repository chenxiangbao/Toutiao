package com.nowcoder.service;

import com.nowcoder.dao.CommentDao;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    public void addComment(Comment comment){
        commentDao.addComment(comment);
    }

    public List<Comment> selectByEntity(int entityId,int entityType){
        List<Comment> commentList = commentDao.selectByEntity(entityId,entityType);
        return commentList;
    }

    public int getCommentCount(int entityId,int entityType){
        int count = commentDao.getCommentCount(entityId,entityType);
        return count;
    }

    public void deleteComment(int id,int entityId,int entityType){
        commentDao.updateComment(id,entityId,entityType,1);
    }

    public Comment selectById(int id){
        return commentDao.selectById(id);
    }
}
