package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;  // 唯一标识符

    private String name;  // 折扣名称

    private Integer count;  // 折扣数量

    // 其他字段可以根据需求添加，例如：有效期、折扣类型等

}
