package com.sky.controller.admin;

// src/main/java/com/yourpackage/controller/ZhuanyuanController.java
import com.sky.dto.PasswordEditDTO;
import com.sky.dto.RewardPunishDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.ZhuanyuanService ;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/zhuanyuan")
@Api(tags="专员相关接口")
public class ZhuanyuanController {

    @Autowired
    private ZhuanyuanService zhuanyuanService;

    @GetMapping("/list")
    @ApiOperation("专员信息列表")
    public Result<PageResult> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone
    ) {
        PageResult pageResult = zhuanyuanService.list(page, pageSize, name, phone);
        return Result.success(pageResult);
    }

    @ApiOperation("删除专员")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteZhuanyuan(@PathVariable("id") Long id) {
        try {
            zhuanyuanService.deleteAndUpdateZystatus(id);
            return ResponseEntity.ok("删除并更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败，记录不存在");
        }
    }

    @PostMapping("/reward")
    @ApiOperation("专员奖励接口")
    public Result rewardAdd(@RequestBody RewardPunishDTO dto){
        zhuanyuanService.addReward(dto);
        return Result.success("奖励设置成功");
    }

    @PostMapping("/punish")
    @ApiOperation("专员惩罚接口")
    public Result PunishAdd(@RequestBody RewardPunishDTO dto){
        zhuanyuanService.addPunish(dto);
        return Result.success("惩罚设置成功");
    }
}
