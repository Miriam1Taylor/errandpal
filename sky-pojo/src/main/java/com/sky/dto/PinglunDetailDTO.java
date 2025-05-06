package com.sky.dto;

public class PinglunDetailDTO {
    private Integer id;
    private String details;
    private Integer shequid;
    private Long userid;
    private Integer likeCount;
    private String name;     // from user table
    private String avatar;   // from user table

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public Integer getShequid() { return shequid; }
    public void setShequid(Integer shequid) { this.shequid = shequid; }

    public Long getUserid() { return userid; }
    public void setUserid(Long userid) { this.userid = userid; }

    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}