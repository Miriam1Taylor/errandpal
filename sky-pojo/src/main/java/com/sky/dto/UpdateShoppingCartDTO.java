package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UpdateShoppingCartDTO implements Serializable {
    private String name;
    private String image;
    private BigDecimal amount;

    private Long dishId;
    private Long setmealId;
    private String dishFlavor;

}
