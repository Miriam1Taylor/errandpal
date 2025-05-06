package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.entity.Discount;
import com.sky.mapper.AdminDiscountMapper;
import com.sky.result.PageResult;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AdminDiscountService {

//    // 批量插入折扣数据
//    void insertBatch(List<Discount> discounts);
    void insertBatch(Discount discount);

    boolean updateDiscount(Discount discount);

    // 更新userdisc表的方法
    void updateUserDiscount(Long id, Long orderid);

    List<Discount> getUserDiscounts(Integer iszy);

    Integer getZyStatusById(Long userId);

    Page<Discount> pageQuery(String name, String status, int page, int pageSize);

    //插入数据到用户优惠券关联表
    boolean receiveUserDiscounts();
//    // 获取所有折扣数据
//    List<Discount> getAllDiscounts();
//    /**
//     * 历史订单查询
//     *
//     * @param pageNum
//     * @param pageSize
//     * @param status   订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
//     * @return
//     */
//    PageResult pageQueryForUser(int pageNum, int pageSize, Integer status);
    // 根据ID删除折扣数据
    void deleteById(Long id);

    // 根据ID获取折扣数据
    Discount getDiscountById(Long id);


}
