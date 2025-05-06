package com.sky.dto;

import lombok.Data;

@Data
public class ShequDetailDTO {
    private Integer id;
    private String title;
    private String details;
    private Long userid;
    private Integer likeCount;
    private String name;     // from user table
    private String avatar;   // from user table
    private Integer isliked;      // from usershe table
}