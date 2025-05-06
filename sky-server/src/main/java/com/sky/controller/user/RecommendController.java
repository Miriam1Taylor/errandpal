package com.sky.controller.user;

import com.sky.context.UserBaseContext;
import com.sky.dto.ZhuanyuanRecommendDTO;
import com.sky.service.RecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "C端-协同过滤算法")
@RequestMapping("/user/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @ApiOperation("专员推荐算法")
    @GetMapping
    public List<ZhuanyuanRecommendDTO> getRecommendations() {// 获取当前登录用户id
        Long userId = UserBaseContext.getCurrentId();
        return recommendService.recommendForUser(userId);
    }
}
