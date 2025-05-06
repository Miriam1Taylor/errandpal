package com.sky.service;

import com.sky.dto.PinglunDetailDTO; 
import com.sky.entity.Pinglun;

import java.util.List;

// ===== service/PinglunService.java =====
public interface PinglunService {
    void publishComment(String details, Integer shequid, Long userId);
    void like(Long id);
    void delete(Integer id);

    List<PinglunDetailDTO> getPinglunByShequId(Integer shequId);

}
