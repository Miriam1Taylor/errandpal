package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.mapper.PasswordMapper;
import com.sky.service.PasswordEditService;
import com.sky.exception.PasswordErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

@Service
public class PasswordEditImpl implements PasswordEditService {

    @Autowired
    private PasswordMapper passwordMapper;

    @Override
    public void passedit(PasswordEditDTO dto) {
        // 获取当前登录员工ID
        Long empId = BaseContext.getCurrentId();

        System.out.println(empId);
        // 获取数据库中的旧员工信息
        Employee dbEmployee = passwordMapper.getById(empId);

        // 校验旧密码是否正确
        String inputOldPassword = DigestUtils.sha256Hex(dto.getOldPassword().getBytes());
        System.out.println(inputOldPassword);
        if (!inputOldPassword.equals(dbEmployee.getPassword())) {
            throw new PasswordErrorException("原始密码错误");
        }

        // 加密新密码并更新
        String newPass = DigestUtils.sha256Hex(dto.getNewPassword().getBytes());
        passwordMapper.updatePassword(empId, newPass);
    }
}