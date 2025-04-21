package com.sky.service;

import com.sky.entity.Discount;

import java.util.List;

public interface AdminDiscountService {

    // 批量插入折扣数据
    void insertBatch(List<Discount> discounts);

    // 根据ID删除折扣数据
    void deleteById(Long id);

    // 根据ID获取折扣数据
    Discount getDiscountById(Long id);

    // 获取所有折扣数据
    List<Discount> getAllDiscounts();
}
