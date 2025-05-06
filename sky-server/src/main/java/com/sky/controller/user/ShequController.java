package com.sky.controller.user;

import com.sky.dto.ShequDetailDTO;
import com.sky.dto.ShequUserDTO;
import com.sky.entity.Shequ;
import com.sky.result.Result;
import com.sky.service.ShequService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "C端-社区帖子相关接口")
@RequestMapping("/user/shequ")
public class ShequController {
    @Autowired
    private ShequService shequService;

    @ApiOperation("发布帖子")
    @PostMapping("/post")
    public String post(@RequestBody Shequ shequ) {
        shequService.post(shequ);
        return "帖子发布成功";
    }

    @ApiOperation("帖子点赞")
    @PostMapping("/like")
    public String like(@RequestParam Integer id) {
        shequService.like(id);
        return "点赞成功";
    }

    @ApiOperation("删除帖子")
    @DeleteMapping("/delete")
    public String delete(@RequestParam Integer id) {
        shequService.delete(id);
        return "帖子删除成功";
    }

    @ApiOperation("帖子列表")
    @GetMapping("/list")
    public Result<List<ShequUserDTO>> listShequWithUserName() {
        List<ShequUserDTO> list = shequService.listShequWithUserName();
        return Result.success(list);
    }

    @ApiOperation("获取帖子详情")
    @GetMapping("/{id}")
    public Result<ShequDetailDTO> getShequDetail(@PathVariable("id") Integer id) {
        ShequDetailDTO detail = shequService.getShequDetail(id);
        return Result.success(detail);
    }

}