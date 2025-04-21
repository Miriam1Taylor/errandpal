package com.sky.service.impl;

import com.sky.entity.Discount;
import com.sky.mapper.AdminDiscountMapper;
import com.sky.service.AdminDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDiscountServiceImpl implements AdminDiscountService {

    @Autowired
    private AdminDiscountMapper discountMapper;

    @Override
    public void insertBatch(List<Discount> discounts) {
        discountMapper.insertBatch(discounts);
    }

    @Override
    public void deleteById(Long id) {
        discountMapper.deleteById(id);
    }

    @Override
    public Discount getDiscountById(Long id) {
        return discountMapper.getById(id);
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discountMapper.getAll();
    }
}
