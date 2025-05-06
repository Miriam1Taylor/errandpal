package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.PinglunDetailDTO;
import com.sky.entity.Pinglun;
import com.sky.mapper.PinglunMapper;
import com.sky.service.PinglunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PinglunServiceImpl implements PinglunService {
    @Autowired
    private PinglunMapper pinglunMapper;

    @Override
    public void publishComment(String details, Integer shequid, Long userId) {
        Pinglun pinglun = new Pinglun();
        pinglun.setDetails(details);
        pinglun.setShequid(shequid);
        pinglun.setUserid(userId);
        pinglun.setLikeCount(0);  // 默认点赞数为0
        pinglunMapper.insert(pinglun);  // 插入评论到数据库
    }

    @Override
    public void like(Integer id) {
        pinglunMapper.likePinglun(id);
    }

    @Override
    public void delete(Integer id) {
        pinglunMapper.deletePinglun(id);
    }

    @Override
    public List<PinglunDetailDTO> getPinglunByShequId(Integer shequId) {
        return pinglunMapper.getPinglunByShequId(shequId);
    }
}