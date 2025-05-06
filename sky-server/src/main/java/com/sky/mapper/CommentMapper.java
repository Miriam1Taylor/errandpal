package com.sky.mapper;

import com.sky.annotation.UserAutoFill;
import com.sky.dto.OrderCommentStatusDTO;
import com.sky.entity.Comment;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    // 插入评论，并获取自增的主键ID
    @Insert("INSERT INTO comment (title, details, userid, orderid, create_time, create_user) " +
            "VALUES (#{title}, #{details}, #{userid}, #{orderid}, #{createTime}, #{createUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @UserAutoFill(OperationType.INSERT)
    void insert(Comment comment);

    OrderCommentStatusDTO selectOrderCommentStatusById(Long orderId);


    // 获取所有评论
    @Select("SELECT * FROM comment")
    List<Comment> listAll();

    // 根据用户ID获取该用户所有评论
    @Select("SELECT * FROM comment WHERE userid = #{userid}")
    List<Comment> listByUserId(Long userid);

    // 根据订单ID获取评论
    @Select("SELECT * FROM comment WHERE orderid = #{orderid}")
    Comment getByOrderId(Long orderid);
}