package com.sky.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Discount implements Serializable {

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;  // 唯一标识符

    private String name;  // 折扣名称

    private BigDecimal count;  // 折扣数，支持小数

    private Integer iszy;

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date starttime;  // 折扣开始时间

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;  // 折扣结束时间

    private Integer status;  // 折扣状态

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;  // 创建时间

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //这边公共字段填充更新时间一定要设置成localDateTime,不然就更新不了，识别不到setlocaltime方法（getter、setter方法中的setter）
    private LocalDateTime updateTime;  // 更新时间
//    private LocalDateTime updateTime;

}

