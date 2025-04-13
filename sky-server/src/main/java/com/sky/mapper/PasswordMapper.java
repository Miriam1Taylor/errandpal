package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PasswordMapper {

    /**
     * 根据员工ID获取员工信息（用于校验旧密码）
     * @param empId 员工ID
     * @return 员工对象
     */
    Employee getById(@Param("empId") Long empId);

    /**
     * 根据员工ID修改密码
     * @param empId 员工ID
     * @param newPassword 新密码
     * @return 影响的行数
     */
    int updatePassword(@Param("empId") Long empId, @Param("newPassword") String newPassword);
}