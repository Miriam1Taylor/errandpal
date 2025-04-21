package com.sky.service.impl;

import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService{
    @Autowired
    private UserMapper userMapper;
    private AdminUserService adminUserService;

    @Override
    public void setUserAsZhuanyuan(Long userid) {
        adminUserService.setUserAsZhuanyuan(userid);
    }

    @Override
    public List<User> selectZhuanyuanUsers() {
        return userMapper.selectZhuanyuanUsers();
    }

    @Override
    public List<User> getUsersByNameOrPhone(String name, String phone) {
        return userMapper.selectUsersByNameOrPhone(name, phone);
    }


}
