package com.sky.service.impl;

import com.sky.entity.Comment;
import com.sky.mapper.CommentMapper;
import com.sky.mapper.OrderMapper;
import com.sky.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void addComment(Comment comment, Long userId) {
        comment.setUserid(userId);  // 设置当前用户ID
        commentMapper.insert(comment); // 插入评论
        orderMapper.updateCommentId(comment.getOrderid(), comment.getId()); // 更新订单的 commentid 字段
    }

    @Override
    public List<Comment> getAllComments() {
        return commentMapper.listAll();
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentMapper.listByUserId(userId);
    }

    @Override
    public Comment getCommentByOrderId(Long orderId) {
        return commentMapper.getByOrderId(orderId);
    }
}