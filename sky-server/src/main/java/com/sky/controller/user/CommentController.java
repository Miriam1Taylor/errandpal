package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.Comment;
import com.sky.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "C端-订单评价相关接口")
@RequestMapping("/user/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation("添加评论")
    @PostMapping("/add")
    public String addComment(@RequestBody Comment comment) {
        Long userId = BaseContext.getCurrentId(); // 从登录上下文中获取当前登录用户ID
        commentService.addComment(comment, userId);
        return "评价提交成功";
    }

    private Long getCurrentUserId() {
        // 从ThreadLocal或JWT或Session获取用户ID
        return BaseContext.getCurrentId();
    }
}