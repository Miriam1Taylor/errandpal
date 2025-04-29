package com.sky.controller.admin;

import com.sky.context.BaseContext;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sky.service.AdminUserService;

import java.util.List;

@RestController
@RequestMapping("/admin/review")
@Api(tags = "审核用户为专员")
public class AdminUserController {
    @Autowired
    private UserMapper userMapper;
//    private Logger log;

    @PostMapping("/accept/{id}")
    @ApiOperation("通过审核方法")

    public ResponseEntity<String> accept(@PathVariable Long id) {
        // 更新用户表状态
        userMapper.updateZyStatusById(id);

        // 插入zhuanyuan表
        userMapper.insertZhuanyuan(id);

        return ResponseEntity.ok("操作成功");
    }

    @PostMapping("/refuse/{id}")
    @ApiOperation("审核设置未通过方法")
    public ResponseEntity<String> refuse(@PathVariable Long id) {
        // 更新用户表状态
        userMapper.updateZyStatus2ById(id);

        return ResponseEntity.ok("操作成功");
    }

    @Autowired
    private AdminUserService userService;

    @GetMapping("/list")
    @ApiOperation("获取用户列表方法")
    public List<User> getZhuanyuanUserList() {
        return userService.selectZhuanyuanUsers();
    }

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping("/search")
    @ApiOperation("搜索方法")
    public List<User> getUsers(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String phone) {
        return adminUserService.getUsersByNameOrPhone(name, phone);
    }
}
