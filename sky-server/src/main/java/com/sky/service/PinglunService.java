package com.sky.service;

import com.sky.dto.PinglunDetailDTO;

import java.util.List;

// ===== service/PinglunService.java =====
public interface PinglunService {
    void publishComment(String details, Integer shequid, Long userId);
    int like(Long id);
    void delete(Integer id);

    List<PinglunDetailDTO> getPinglunByShequId(Integer shequId);

}
