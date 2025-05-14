package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponseDTO {
    private String message;
    private int isliked; // 0 代表未点赞，1 代表已点赞
}
