package com.sky.mapper;

import com.sky.dto.ZhuanyuanDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Mapper
public interface ZhuanyuanMapper {
    List<ZhuanyuanDTO> list(@Param("offset") int offset,
                            @Param("limit") int limit,
                            @Param("name") String name,
                            @Param("phone") String phone);

    long count(@Param("name") String name,
               @Param("phone") String phone);

    // 更新奖励：增加评价度和活跃度
    int updateReward(@Param("id") Long id,
                     @Param("evaluation") Integer evaluation,
                     @Param("activity") Integer activity);

    int getActive(@Param("id") Long id);
    int getJudge(@Param("id") Long id);

    // 更新 user 表中的 zystatus
    int updateZystatusTo2(@Param("id") Long id);

    // 删除 zhuanyuan 表中的记录
    int deleteById(Long id);
}
