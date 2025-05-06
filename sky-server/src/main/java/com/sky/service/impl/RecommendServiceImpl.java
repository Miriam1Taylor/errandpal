package com.sky.service.impl;

import com.sky.dto.ZhuanyuanRecommendDTO;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ZhuanyuanMapper;
import com.sky.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private ZhuanyuanMapper zhuanyuanMapper;

    @Override
    public List<ZhuanyuanRecommendDTO> recommendForUser(Long userId) {
        return zhuanyuanMapper.recommendZhuanyuanByDishTaste(userId);
    }
}
