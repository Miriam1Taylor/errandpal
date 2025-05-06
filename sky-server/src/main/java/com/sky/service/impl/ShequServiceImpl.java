package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShequDetailDTO;
import com.sky.entity.Shequ;
import com.sky.dto.ShequUserDTO;
import com.sky.mapper.ShequMapper;
import com.sky.service.ShequService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShequServiceImpl implements ShequService {
    @Autowired
    private ShequMapper shequMapper;

    @Override
    public void post(Shequ shequ) {
        System.out.println(BaseContext.getCurrentId());
        shequ.setUserid(BaseContext.getCurrentId());
        shequMapper.insertShequ(shequ);
    }

    @Override
    public void like(Integer id) {
        shequMapper.likeShequ(id);
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