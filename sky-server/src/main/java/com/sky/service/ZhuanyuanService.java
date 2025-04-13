package com.sky.service;


import com.sky.dto.RewardPunishDTO;
import com.sky.mapper.ZhuanyuanMapper;
import com.sky.result.PageResult;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ZhuanyuanService{
    PageResult list(int page, int pageSize, String name, String phone);

    // 奖励操作
    void addReward(RewardPunishDTO dto);
    // 惩罚操作
    void addPunish(RewardPunishDTO dto);
}

