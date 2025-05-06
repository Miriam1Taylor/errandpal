package com.sky.controller.user;

import com.sky.context.UserBaseContext;
import com.sky.dto.ZhuanyuanDTO;
import com.sky.service.ZhuanyuanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/zhuanyuan")
@Api(tags="C端-专员相关接口")
public class UserZhuanyuanController {
    @Autowired
    private ZhuanyuanService zhuanyuanService;

    @ApiOperation("专员上传审核")
    @DeleteMapping("/renzheng")
    public ResponseEntity<String> deleteZhuanyuan(Long id) {
        try {
            id = UserBaseContext.getCurrentId();
            zhuanyuanService.renzheng(id);
            System.out.println(id);
            return ResponseEntity.ok("认证资料递交成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("认证递交失败，记录不存在");
        }
    }

    @ApiOperation("专员账户余额查看/提现")
    @GetMapping("/list")
    public List<ZhuanyuanDTO> getZhuanyuanList(Long id) {
        id = UserBaseContext.getCurrentId();
        return zhuanyuanService.getZhuanyuanByUserId(id);
    }
}
