package com.sky.service;

import com.sky.dto.ZhuanyuanRecommendDTO;

import java.util.List;

public interface RecommendService {
    List<ZhuanyuanRecommendDTO> recommendForUser(Long userId);
}
