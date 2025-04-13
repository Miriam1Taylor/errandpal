package com.sky.controller.admin;

import com.sky.dto.PasswordEditDTO;
import com.sky.result.Result;
import com.sky.service.PasswordEditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/admin/password")
@Slf4j
@Api(tags = "密码修改接口")

public class PasswordEditController {
    @Autowired
    private PasswordEditService passwordEditService;

    @PutMapping("/editpass")
    @ApiOperation("修改密码")
    public Result passwordEdit(@RequestBody PasswordEditDTO dto){
        passwordEditService.passedit(dto);
        return Result.success("密码修改成功");
    }

}