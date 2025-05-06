package com.sky.entity;

import lombok.Data;

@Data
public class UserPing {
    private int id;
    private Long userId;
    private Long pinglunId;
    private int isliked;
}
