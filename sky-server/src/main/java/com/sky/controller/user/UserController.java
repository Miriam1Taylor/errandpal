package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.UserBaseContext;
import com.sky.dto.CurrentUserDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

/**
 * 微信登录
 */
@RestController
@RequestMapping("/user/user")
@Api(tags = "C端-用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("微信用户登录：{}", userLoginDTO.getCode());

//        微信登录
        User user = userService.wxLogin(userLoginDTO);

//        为微信用户生成jwt令牌
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    @ApiOperation("用户个人信息完善")
    @PostMapping("/updateInfo")
    public Result<String> updateUserInfo(@RequestBody User user) {
        user.setId(UserBaseContext.getCurrentId());
        boolean success = userService.updateUserInfo(user);
        return success ? Result.success("用户信息更新成功") : Result.error("更新失败，请检查id是否存在");
    }

    @ApiOperation("用户个人信息展示")
    @GetMapping("/showInfo/{userId}")
    public Result<User> showUserInfo(@PathVariable  Long userId) {
        User user = userService.getUserInfoById(userId);
        return user != null ? Result.success(user) : Result.error("获取失败，请检查用户是否存在");
    }

    @ApiOperation("获取当前登录用户ID")
    @GetMapping("/getCurrentId")
    public Result<CurrentUserDTO> getCurrentUserId() {
        Long id = UserBaseContext.getCurrentId();
        String id2 = id.toString();
        User user = userMapper.getById(id2); // 请确认你有这个方法

        if (user == null) {
            return Result.error("用户不存在");
        }

        CurrentUserDTO dto = new CurrentUserDTO(user.getId(), user.getName(), user.getAvatar());
        return Result.success(dto); // 只返回 data，不带 message
    }



}
