package com.sky.controller.user;

import com.sky.dto.IsZystatusDTO;
import com.sky.dto.OrdersDTO;
import com.sky.entity.Discount;
import com.sky.result.Result;
import com.sky.service.AdminDiscountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@RestController
@Api(tags ="C端-优惠券相关接口")
@RequestMapping("/user/discount")
public class DiscountController {

    @Autowired
    private AdminDiscountService discountService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("查看是否是专员身份")
    @GetMapping("/zystatus/{userId}")
    public Result<IsZystatusDTO> checkZyStatus(@PathVariable Long userId) {
        Integer status = discountService.getZyStatusById(userId);
        IsZystatusDTO dto = new IsZystatusDTO();
        dto.setId(userId);
        dto.setZystatus((status != null && status == 1) ? 1 : 0);
        return Result.success(dto); // ✅ 包装成标准返回格式
    }

    @ApiOperation("根据用户不同身份展示不同信息")
    @GetMapping("/list")
    public Result<List<Discount>> getDiscounts(@RequestParam(required = false) Integer iszy) {
        List<Discount> list = discountService.getUserDiscounts(iszy);
        return Result.success(list);
    }

    // 根据ID获取折扣
    @ApiOperation("用户领取优惠券")
    @PostMapping("/receive")
    public Result<String> receiveDiscounts() {
        boolean success = discountService.receiveUserDiscounts();
        if (success) {
            return Result.success("优惠券领取成功");
        } else {
            return Result.error("领取失败，可能已经领取或未登录");
        }
    }


    /**
     * 根据用户优惠券的ID更新useat、orderid，并将status改为1
     * @param dto
     * @return 操作结果
     */
    @ApiOperation("使用优惠券")
    @PutMapping("/use")
    public String updateUserDiscount(@RequestBody OrdersDTO dto, @RequestParam Discount discount) throws Exception {
        try {
            Long id = discount.getId();
            Long orderid = dto.getId();

            discountService.updateUserDiscount(id, orderid);
            return "User discount updated successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
