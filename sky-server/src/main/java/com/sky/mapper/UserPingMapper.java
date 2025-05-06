package com.sky.mapper;


import com.sky.entity.UserPing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserPingMapper {
    Integer getIsLiked(@Param("userId") Long userId, @Param("pinglunId") Long pinglunId);
    UserPing getUserPing(@Param("userId") Long userId, @Param("pinglunId") Long pinglunId);
    void insertUserPing(@Param("userId") Long userId, @Param("pinglunId") Long pinglunId, @Param("isLiked") Integer isLiked);
    void updateUserPing(@Param("userId") Long userId, @Param("pinglunId") Long pinglunId, @Param("isLiked") Integer isLiked);
}
