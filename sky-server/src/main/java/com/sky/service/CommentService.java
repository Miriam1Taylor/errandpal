package com.sky.service;

import com.sky.entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment, Long userId);

    List<Comment> getAllComments();

    List<Comment> getCommentsByUserId(Long userId);

    Comment getCommentByOrderId(Long orderId);
}