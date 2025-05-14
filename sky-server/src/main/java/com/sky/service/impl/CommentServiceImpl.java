package com.sky.service.impl;

import com.sky.dto.OrderCommentStatusDTO;
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
        // 插入后，获取评论ID（假设插入成功后 comment.id 会被自动回填）
        orderMapper.updateOrderComment(comment.getId(), comment.getJudge(), comment.getOrderid());
        orderMapper.updateZhuanyuanJudge(comment.getJudge(), comment.getOrderid());

    }
    @Override
    public OrderCommentStatusDTO getCommentStatusByOrderId(Long orderId) {
        return commentMapper.selectOrderCommentStatusById(orderId);
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