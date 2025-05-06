package com.sky.entity;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private String title;
    private String details;
    private Long userid;
    private Long orderid;
}