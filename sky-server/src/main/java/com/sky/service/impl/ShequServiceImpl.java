package com.sky.service.impl;

import com.sky.context.UserBaseContext;
import com.sky.dto.ShequDetailDTO;
import com.sky.entity.Shequ;
import com.sky.dto.ShequUserDTO;
import com.sky.entity.UserShe;
import com.sky.mapper.ShequMapper;
import com.sky.mapper.UserSheMapper;
import com.sky.service.ShequService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShequServiceImpl implements ShequService {
    @Autowired
    private ShequMapper shequMapper;

    @Autowired
    private UserSheMapper userSheMapper;

    @Override
    public void post(Shequ shequ) {
        System.out.println(UserBaseContext.getCurrentId());
        shequ.setUserid(UserBaseContext.getCurrentId());
        shequMapper.insertShequ(shequ);
    }

    @Override
    public int like(Long shequId) {
        Long userId = UserBaseContext.getCurrentId();
        UserShe userShe = userSheMapper.getUserShe(userId, shequId);

        if (userShe == null) {
            userSheMapper.insertUserShe(userId, shequId, 1);
            shequMapper.incrementLikeCount(shequId);
            return 1; // 现在是点赞状态
        } else if (userShe.getIsliked() == 0) {
            userSheMapper.updateUserShe(userId, shequId, 1);
            shequMapper.incrementLikeCount(shequId);
            return 1;
        } else {
            userSheMapper.updateUserShe(userId, shequId, 0);
            shequMapper.decrementLikeCount(shequId);
            return 0; // 现在是取消点赞状态
        }
    }



    @Override
    public void delete(Integer id) {
        shequMapper.deleteShequ(id);
    }


    @Override
    public List<ShequUserDTO> listShequWithUserName() {
        return shequMapper.getShequWithUserName();
    }
    @Override
    public ShequDetailDTO getShequDetail(Integer id) {
        return shequMapper.getShequDetailById(id);
    }

}