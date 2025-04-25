package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.entity.Discount;
import com.sky.mapper.AdminDiscountMapper;
import com.sky.result.PageResult;
import com.sky.service.AdminDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AdminDiscountServiceImpl implements AdminDiscountService {

    @Autowired
    private AdminDiscountMapper discountMapper;

    @Override
    public void insertBatch(Discount discount) {
        discountMapper.insertBatch(discount);
    }

    @Override
    public boolean updateDiscount(Discount discount) {
        return discountMapper.updateDiscount(discount) > 0;
    }

    @Override
    public Page<Discount> pageQuery(String name, String status, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);  // 启动分页
        return discountMapper.getAll(name, status);  // 返回 Page 对象
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return Collections.emptyList();
    }

    @Override
    public PageResult pageQueryForUser(int pageNum, int pageSize, Integer status) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        discountMapper.deleteById(id);
    }

    @Override
    public Discount getDiscountById(Long id) {
        return discountMapper.getById(id);
    }

//    @Override
//    public List<Discount> getAllDiscounts() {
//        return discountMapper.getAll();
//    }
}
