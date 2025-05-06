package com.sky.dto;

import lombok.Data;

@Data
public class ZhuanyuanRecommendDTO {
    private Long zhuanyuanId;
    private String zhuanyuanName;
    private Double weightedScore;
    private Integer orderCount;
}
