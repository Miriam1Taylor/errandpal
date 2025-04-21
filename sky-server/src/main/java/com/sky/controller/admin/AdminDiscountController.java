package com.sky.controller.admin;

import com.sky.entity.Discount;
import com.sky.service.AdminDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/discount")
public class AdminDiscountController {

    @Autowired
    private AdminDiscountService discountService;

    // 获取所有折扣
    @GetMapping("/all")
    public List<Discount> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    // 根据ID获取折扣
    @GetMapping("/{id}")
    public Discount getDiscountById(@PathVariable Long id) {
        return discountService.getDiscountById(id);
    }

    // 批量插入折扣
    @PostMapping("/batch")
    public String insertBatch(@RequestBody List<Discount> discounts) {
        discountService.insertBatch(discounts);
        return "Batch insert successful!";
    }

    // 删除折扣
    @DeleteMapping("/{id}")
    public String deleteDiscount(@PathVariable Long id) {
        discountService.deleteById(id);
        return "Discount deleted successfully!";
    }
}
