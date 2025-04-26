package com.sky.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 审核总览
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewOverViewVO implements Serializable {

    // 未审核数量
    private Integer unreviewed;
}
