package com.sky.mapper;

import com.sky.entity.UserDisc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserDiscountMapper {

    int batchInsert(@Param("list") List<UserDisc> list);

    // 更新userdisc表，插入useat、orderid并更新status
    void updateUserDiscount(Map<String, Object> params);

    void updateExpiredCoupons(@Param("now") LocalDateTime now);

    void updateExpiredCoupons2(@Param("now") LocalDateTime now);

}
