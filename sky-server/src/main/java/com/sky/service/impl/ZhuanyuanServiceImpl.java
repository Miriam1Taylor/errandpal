package com.sky.service.impl;

import com.sky.dto.PasswordEditDTO;
import com.sky.dto.RewardPunishDTO;
import com.sky.dto.ZhuanyuanDTO;
import com.sky.mapper.ZhuanyuanMapper;
import com.sky.result.PageResult;
import com.sky.service.ZhuanyuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ZhuanyuanServiceImpl implements ZhuanyuanService {

    @Autowired
    private ZhuanyuanMapper zhuanyuanMapper;

    @Override
    public PageResult list(int page, int pageSize, String name, String phone) {
        int offset = (page - 1) * pageSize;
        List<ZhuanyuanDTO> data = zhuanyuanMapper.list(offset, pageSize, name, phone);
        long total = zhuanyuanMapper.count(name, phone);
        return new PageResult(total, data);
    }

    // 实现奖励操作
    @Override
    public void addReward(RewardPunishDTO dto){
        Long id = dto.getId();
//        System.out.println(id);

        int ac = dto.getActivity();
        int ev = dto.getEvaluation();

        int judge = zhuanyuanMapper.getJudge(id);
        int active = zhuanyuanMapper.getActive(id);
//        System.out.println(judge);
//        System.out.println(active);

        zhuanyuanMapper.updateReward(id,ev+judge,active+ac);
    }


    @Transactional
    @Override
    public void addPunish(RewardPunishDTO dto){
        Long id = dto.getId();

        int ac = dto.getActivity();
        int ev = dto.getEvaluation();

        int judge = zhuanyuanMapper.getJudge(id);
        int active = zhuanyuanMapper.getActive(id);

        int newJudge = judge - ev;
        int newActive = active - ac;

        if (newJudge < 0 || newActive < 0) {
            throw new RuntimeException("惩罚后评价度或活跃度不能为负！");
        }

        zhuanyuanMapper.updateReward(id, newJudge, newActive);
    }


}
