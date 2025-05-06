package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.annotation.UserAutoFill;
import com.sky.entity.User;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    int updateZyStatusById(@Param("id") Long id);
    int updateZyStatus2ById(@Param("id") Long id);
    int insertZhuanyuan(@Param("id") Long id);

    List<User> selectZhuanyuanUsers();

    // 根据姓名或电话模糊查询用户
    List<User> selectUsersByNameOrPhone(String name, String phone);
    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    User getById(String id);

    /**
     * 根据openid获取当前用户
     * @param openid
     * @return
     */
    User getByOpenId(@Param("openid") String openid);

    /**
     * 创建新用户
     * @param user
     */
    @AutoFill(OperationType.INSERT)
    void insert(User user);

    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    int updateUserInfoByOpenid(User user);
}
