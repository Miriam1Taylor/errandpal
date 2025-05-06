package com.sky.dto;

import lombok.Data;

@Data
public class PinglunDetailDTO {
    private Integer id;
    private String details;
    private Integer shequid;
    private Long userid;
    private Integer likeCount;
    private String name;     // from user table
    private String avatar;   // from user table
    private Integer isliked;    // from userping table
}