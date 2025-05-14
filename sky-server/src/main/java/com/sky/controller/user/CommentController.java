package com.sky.controller.user;

import com.sky.context.UserBaseContext;
import com.sky.dto.OrderCommentStatusDTO;
import com.sky.entity.Comment;
import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "C端-订单评价相关接口")
@RequestMapping("/user/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation("添加评论")
    @PostMapping("/add")
    public Result<String> addComment(@RequestBody Comment comment) {
        Long userId = UserBaseContext.getCurrentId(); // 获取当前用户ID

        // 根据订单ID获取评论状态
        OrderCommentStatusDTO dto = commentService.getCommentStatusByOrderId(comment.getOrderid());

        // 判断状态是否为 "暂未评论"
        if (dto != null && "暂未评论".equals(dto.getCommentStatus())) {
            commentService.addComment(comment, userId);
            return Result.success("评价提交成功");
        } else {
            // 抛出自定义异常（或返回错误结果）
            return Result.error("无法进行此操作");
        }
    }


    @ApiOperation("获取当前订单是否已评价")
    @PostMapping("/comment-status")
    public Result<OrderCommentStatusDTO> getCommentStatus(@RequestBody Orders order) {
        Long orderId = order.getId();
        OrderCommentStatusDTO dto = commentService.getCommentStatusByOrderId(orderId);
        System.out.println(dto);
        return Result.success(dto);
    }

}