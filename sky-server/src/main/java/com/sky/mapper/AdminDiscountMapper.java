package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Discount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminDiscountMapper {

    // 批量插入折扣数据
    void insertBatch(Discount discount);

    // 根据id删除折扣数据
    void deleteById(Long id);

    // 根据id查询折扣数据
    Discount getById(Long id);

    // 查询所有折扣数据
    Page<Discount> getAll(@Param("name") String name, @Param("status") String status);

    int updateDiscount(Discount discount);
}
