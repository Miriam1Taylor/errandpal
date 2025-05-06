package com.sky.mapper;

import com.sky.dto.ShequDetailDTO;
import com.sky.dto.ShequUserDTO;
import com.sky.entity.Shequ;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShequMapper {
    void insertShequ(Shequ shequ);
    void likeShequ(@Param("id") Integer id);
    void deleteShequ(@Param("id") Integer id);

    List<ShequUserDTO> getShequWithUserName();
    ShequDetailDTO getShequDetailById(@Param("id") Integer id);

}