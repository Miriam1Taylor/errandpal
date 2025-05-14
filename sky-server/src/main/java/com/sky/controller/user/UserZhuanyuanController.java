package com.sky.controller.user;

import com.sky.context.UserBaseContext;
import com.sky.dto.ZhuanyuanDTO;
import com.sky.result.Result;
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
    @PostMapping("/renzheng")
    public ResponseEntity<Result<String>> deleteZhuanyuan() {
        try {
            Long id = UserBaseContext.getCurrentId();
            zhuanyuanService.renzheng(id);
            System.out.println(id);
            // 使用 Result 包裹响应
            return ResponseEntity.ok(Result.success("认证资料递交成功"));
        } catch (Exception e) {
            // 使用 Result 包裹失败响应
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Result.error("认证递交失败，记录不存在"));
        }
    }


    @ApiOperation("专员账户余额查看/提现")
    @GetMapping("/list")
    public ResponseEntity<Result<List<ZhuanyuanDTO>>> getZhuanyuanList() {
        try {
            Long id = UserBaseContext.getCurrentId();
            List<ZhuanyuanDTO> zhuanyuanList = zhuanyuanService.getZhuanyuanByUserId(id);

            // 判断数据是否为空，根据情况返回相应的响应
            if (zhuanyuanList != null && !zhuanyuanList.isEmpty()) {
                return ResponseEntity.ok(Result.success(zhuanyuanList));
            } else {
                return ResponseEntity.ok(Result.error("未找到专员账户余额记录"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Result.error("获取账户余额失败"));
        }
    }

    @ApiOperation("专员账户余额提现后更新")
    @GetMapping("/withdraw")
    public ResponseEntity<Result<String>> withdraw() {
        try {
            Long id = UserBaseContext.getCurrentId();
            zhuanyuanService.withdrawById(id);

            return ResponseEntity.ok(Result.success("提现成功"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Result.error("提现失败"));
        }
    }

}
