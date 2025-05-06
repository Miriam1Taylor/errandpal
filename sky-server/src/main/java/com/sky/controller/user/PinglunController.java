package com.sky.controller.user;

import com.sky.context.UserBaseContext;
import com.sky.dto.LikeRequestDTO;
import com.sky.dto.PinglunDTO;
import com.sky.dto.PinglunDetailDTO;
import com.sky.entity.Pinglun;
import com.sky.result.Result;
import com.sky.service.PinglunService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "C端-评论相关接口")
@RequestMapping("/user/pinglun")
public class PinglunController {
    @Autowired
    private PinglunService pinglunService;

    @ApiOperation("发布评论")
    @PostMapping("/comment")
    public Result<String> comment(@RequestBody PinglunDTO commentDTO) {
        try {
            // 获取当前用户ID，假设 BaseContext 是你的工具类
            Long userId = UserBaseContext.getCurrentId();
            System.out.println("userID::"+userId);
            // 调用 Service 层插入评论
            pinglunService.publishComment(commentDTO.getDetails(), commentDTO.getShequid(), userId);
            return Result.success("评论成功");
        } catch (Exception e) {
            // 打印详细错误信息
//            e.printStackTrace();
            return Result.error("未知错误");
        }
    }

    @ApiOperation("评论点赞")
    @PostMapping("/like")
    public Result<String> like(@RequestBody LikeRequestDTO dto) {
        try {
            pinglunService.like(dto.getId());
            return Result.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("操作失败");
        }
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/delete")
    public Result<String> delete(@RequestBody Integer id) {
        try {
            pinglunService.delete(id);
            return Result.success("评论删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("评论删除失败");
        }
    }


    @ApiOperation("评论详情")
    @GetMapping("/shequ/{shequId}")
    public Result<List<PinglunDetailDTO>> getPinglunByShequId(@PathVariable("shequId") Integer shequId) {
        List<PinglunDetailDTO> list = pinglunService.getPinglunByShequId(shequId);
        return Result.success(list);
    }

}
