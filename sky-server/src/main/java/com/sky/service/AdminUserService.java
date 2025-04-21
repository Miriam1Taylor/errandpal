package com.sky.service;

import com.sky.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AdminUserService {
    void setUserAsZhuanyuan(Long id);
    List<User> selectZhuanyuanUsers();

    List<User> getUsersByNameOrPhone(String name, String phone);

}
