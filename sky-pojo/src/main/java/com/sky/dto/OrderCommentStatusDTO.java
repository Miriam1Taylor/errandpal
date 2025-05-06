package com.sky.dto;

import lombok.Data;

@Data
public class OrderCommentStatusDTO {
    private Long orderId;
    private String commentStatus;
}
