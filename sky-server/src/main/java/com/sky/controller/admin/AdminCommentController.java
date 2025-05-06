package com.sky.controller.admin;

import com.sky.entity.Comment;
import com.sky.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "订单评论相关接口")
@RequestMapping("/comment")
public class AdminCommentController {

    @Autowired
    private CommentService commentService;

    // 获取全部评价
    @ApiOperation("获取全部评价")
    @GetMapping("/list")
    public List<Comment> listAll() {
        return commentService.getAllComments();
    }

    // 根据用户ID查询评价
    @ApiOperation("根据用户ID查询评价")
    @GetMapping("/user/{userId}")
    public List<Comment> listByUserId(@PathVariable Long userId) {
        return commentService.getCommentsByUserId(userId);
    }

    // 根据订单ID查询评价
    @ApiOperation("根据订单ID查询评价")
    @GetMapping("/order/{orderId}")
    public Comment getByOrderId(@PathVariable Long orderId) {
        return commentService.getCommentByOrderId(orderId);
    }
}
