package com.sky.mapper;

import com.sky.entity.UserPing;
import com.sky.entity.UserShe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserSheMapper {
    Integer getIsLiked(@Param("userId") Long userId, @Param("shequId") Long shequId);

    UserShe getUserShe(@Param("userId") Long userId, @Param("shequId") Long shequId);

    void insertUserShe(@Param("userId") Long userId, @Param("shequId") Long shequId, @Param("isLiked") Integer isLiked);

    void updateUserShe(@Param("userId") Long userId, @Param("shequId") Long shequId, @Param("isLiked") Integer isLiked);
}
