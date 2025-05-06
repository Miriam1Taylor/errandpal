package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.entity.Discount;
import com.sky.entity.UserDisc;
import com.sky.mapper.AdminDiscountMapper;
import com.sky.mapper.UserDiscountMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.PageResult;
import com.sky.service.AdminDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminDiscountServiceImpl implements AdminDiscountService {

    @Autowired
    private AdminDiscountMapper discountMapper;

    @Autowired
    private UserDiscountMapper userDiscountMapper;

    @Autowired
    private AdminDiscountService discountService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertBatch(Discount discount) {
        discountMapper.insertBatch(discount);
    }

    @Override
    public boolean updateDiscount(Discount discount) {
        return discountMapper.updateDiscount(discount) > 0;
    }

    @Override
    public void updateUserDiscount(Long id, Long orderid) {
        // 获取当前时间作为 useat
        LocalDateTime now = LocalDateTime.now();

        // 准备参数
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("now", now);
        params.put("orderid", orderid);

        // 调用Mapper方法执行更新操作
        userDiscountMapper.updateUserDiscount(params);
    }

    @Override
    public List<Discount> getUserDiscounts(Integer iszy) {
        Map<String, Object> params = new HashMap<>();

        if (iszy != null) {
            if (iszy == 0) {
                params.put("onlyIszy", 0);
            } else if (iszy == 1) {
                params.put("includeIszy", true);
            }
        }

        params.put("now", LocalDateTime.now());

        return discountMapper.getValidDiscounts(params);
    }
    @Override
    public boolean receiveUserDiscounts() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return false; // 防止空指针
        }

        String id = userId.toString();
        List<Discount> discounts;

        if (userMapper.getById(id).getZystatus() == 1) {
            discounts = discountService.getUserDiscounts(1);
        } else {
            discounts = discountService.getUserDiscounts(0);
        }

        if (discounts == null || discounts.isEmpty()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        List<UserDisc> inserts = discounts.stream().map(d -> {
            LocalDateTime endTime = d.getEndtime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            UserDisc ud = new UserDisc();
            ud.setDisId(d.getId());
            ud.setUserid(userId);
            ud.setStatus(endTime.isBefore(now) ? 2 : 0);
            return ud;
        }).collect(Collectors.toList());

        int count = userDiscountMapper.batchInsert(inserts);
        return count > 0;
    }



    @Override
    public Page<Discount> pageQuery(String name, String status, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);  // 启动分页
        return discountMapper.getAll(name, status);  // 返回 Page 对象
    }


    @Override
    public void deleteById(Long id) {
        discountMapper.deleteById(id);
    }

    @Override
    public Integer getZyStatusById(Long userId) {
        return discountMapper.getZyStatusById(userId);
    }

    @Override
    public Discount getDiscountById(Long id) {
        return discountMapper.getById(id);
    }

}
