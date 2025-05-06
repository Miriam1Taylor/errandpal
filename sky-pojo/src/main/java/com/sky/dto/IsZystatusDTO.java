package com.sky.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IsZystatusDTO {

    private Long id;

    private Integer zystatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getZystatus() {
        return zystatus;
    }

    public void setZystatus(Integer zystatus) {
        this.zystatus = zystatus;
    }
}
