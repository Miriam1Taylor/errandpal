package com.sky.mapper;

import com.sky.entity.Discount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminDiscountMapper {

    // 批量插入折扣数据
    void insertBatch(List<Discount> discounts);

    // 根据id删除折扣数据
    void deleteById(Long id);

    // 根据id查询折扣数据
    Discount getById(Long id);

    // 查询所有折扣数据
    List<Discount> getAll();
}
