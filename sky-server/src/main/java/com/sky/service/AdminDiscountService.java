package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.entity.Discount;
import com.sky.result.PageResult;

import java.util.List;

public interface AdminDiscountService {

//    // 批量插入折扣数据
//    void insertBatch(List<Discount> discounts);

    void insertBatch(Discount discount);

    boolean updateDiscount(Discount discount);

    Page<Discount> pageQuery(String name, String status, int page, int pageSize);


    // 获取所有折扣数据
    List<Discount> getAllDiscounts();
    /**
     * 历史订单查询
     *
     * @param pageNum
     * @param pageSize
     * @param status   订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     * @return
     */
    PageResult pageQueryForUser(int pageNum, int pageSize, Integer status);
    // 根据ID删除折扣数据
    void deleteById(Long id);

    // 根据ID获取折扣数据
    Discount getDiscountById(Long id);


}
