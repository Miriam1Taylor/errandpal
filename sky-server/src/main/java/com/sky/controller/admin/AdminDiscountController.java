package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.entity.Discount;
import com.sky.service.AdminDiscountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags ="优惠券相关接口")
@RequestMapping("/admin/discount")
public class AdminDiscountController {

    @Autowired
    private AdminDiscountService discountService;

    @ApiOperation("读取所有优惠券")
    @GetMapping("/page")
    public Page<Discount> pageQuery(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String status,
                                    @RequestParam int page,
                                    @RequestParam int pageSize) {
        return discountService.pageQuery(name, status, page, pageSize);
    }

    @PutMapping("/update")
    public String updateDiscount(@RequestBody Discount discount) {
        boolean success = discountService.updateDiscount(discount);
        return success ? "更新成功" : "更新失败";
    }
//    // 获取所有折扣
//    @ApiOperation("读取所有优惠券")
//    @GetMapping("/all")
//    public List<Discount> getAllDiscounts() {
//        return discountService.getAllDiscounts();
//    }

    // 根据ID获取折扣
    @ApiOperation("根据ID获取优惠券")
    @GetMapping("/{id}")
    public Discount getDiscountById(@PathVariable Long id) {
        return discountService.getDiscountById(id);
    }

    // 插入折扣
    @ApiOperation("插入优惠券")
    @PostMapping("/batch")
    public String insertBatch(@RequestBody Discount discount) {
        discountService.insertBatch(discount);
        return "数据插入成功!";
    }

    // 删除折扣
    @ApiOperation("删除优惠券")
    @DeleteMapping("/{id}")
    public String deleteDiscount(@PathVariable Long id) {
        discountService.deleteById(id);
        return "删除优惠券成功!";
    }
}
