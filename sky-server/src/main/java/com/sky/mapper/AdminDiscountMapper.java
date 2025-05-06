package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.annotation.UserAutoFill;
import com.sky.entity.Discount;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminDiscountMapper {
    /**
     * 插入折扣数据
     * @param discount
     * @return
     */
    // 批量插入折扣数据
    @UserAutoFill(OperationType.INSERT)
    void insertBatch(Discount discount);

    Integer getZyStatusById(Long userId);

    List<Discount> getValidDiscounts(Map<String, Object> params);

    // 根据id删除折扣数据
    void deleteById(Long id);

    // 根据id查询折扣数据
    Discount getById(Long id);

    // 查询所有折扣数据
    Page<Discount> getAll(@Param("name") String name, @Param("status") String status);
    /**
     * 更新折扣数据
     * @param discount
     * @return
     */
    @UserAutoFill(OperationType.UPDATE)
    int updateDiscount(Discount discount);


}
