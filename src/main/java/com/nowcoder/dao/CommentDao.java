package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {
    String TABLE_NAME = "comment";
    String INSET_FIELDS = " content, user_id, create_date, status, entity_id, entity_type ";
    String SELECT_FIELDS = " id, content, user_id, create_date, status, entity_id, entity_type ";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS,
            ") values (#{content},#{userId},#{createDate},#{status},#{entityId},#{entityType})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Comment selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id=#{userId}"})
    Comment selectByUserId(int userId);

    @Update({"update ",TABLE_NAME, " set status=#{status} where entity_id=#{entityId}  and  entity_type=#{entityType} and id=#{id}"})
    void updateComment(@Param("id") int id ,@Param("entityId") int entityId,@Param("entityType") int entityType,@Param("status") int status);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where entity_id=#{entityId}  and  entity_type=#{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Select({"select count(id) from",TABLE_NAME ,"where entity_id=#{entityId}  and  entity_type=#{entityType} and status=0"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);

}
