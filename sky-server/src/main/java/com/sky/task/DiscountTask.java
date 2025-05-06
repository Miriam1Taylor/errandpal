package com.sky.task;

import com.sky.mapper.UserDiscountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义定时任务类
 */
@Component
@Slf4j
public class DiscountTask {

    @Autowired
    private UserDiscountMapper userDiscountMapper;
    /**
     * 定时任务 每隔5秒触发一次
     */
    @Scheduled(cron = "0/15  * * * * ?")
    public void executeTask(){
        log.info("定时任务折扣券状态更新：{}",new Date());
        //TODO 这边业务要开，但是测试防止工作台内容太多先注释掉，这两行都得用
//        userDiscountMapper.updateExpiredCoupons(LocalDateTime.now());
//        userDiscountMapper.updateExpiredCoupons2(LocalDateTime.now());
    }
}

