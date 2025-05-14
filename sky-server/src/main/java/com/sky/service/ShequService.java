package com.sky.service;

import com.sky.dto.ShequDetailDTO;
import com.sky.dto.ShequUserDTO;
import com.sky.entity.Shequ;
import java.util.List;

public interface ShequService {
    void post(Shequ shequ);
    int like(Long id);
    void delete(Integer id);

    List<ShequUserDTO> listShequWithUserName();
    ShequDetailDTO getShequDetail(Integer id);

}
