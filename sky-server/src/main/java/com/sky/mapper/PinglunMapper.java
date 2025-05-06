package com.sky.mapper;

import com.sky.dto.PinglunDetailDTO;
import com.sky.entity.Pinglun;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PinglunMapper {
    void insert(Pinglun pinglun);
    void likePinglun(@Param("id") Integer id);     // +1
    void unlikePinglun(@Param("id") Integer id);   // -1

    void incrementLikeCount(@Param("id") Long id);

    void decrementLikeCount(@Param("id") Long id);
    void deletePinglun(@Param("id") Integer id);

    List<PinglunDetailDTO> getPinglunByShequId(@Param("shequId") Integer shequId);

}