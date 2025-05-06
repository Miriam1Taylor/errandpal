package com.sky.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDisc {
    private Long id;

    private Long disId;

    private Long userid;

    private Integer status;

    // 优惠券使用时间
    private LocalDateTime useat;

    private Long orderid;
}
