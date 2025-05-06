package com.sky.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;
    private String title;
    private String details;
    private Long userid;
    private Long orderid;
    //注册时间
    private LocalDateTime createTime;
    private Long createUser;

    private Integer zystatus;

    public void setCreateTime(LocalDateTime time) {
        this.createTime = time;
    }
    public void setCreateUser(Long user) {
        this.createUser = user;
    }
}