package com.ecnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * 心理评估表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assessment implements Serializable {

    //评估唯一标识
    private Long assessmentId;

    //用户ID
    private Long userId;

    //评估类型（如焦虑量表）
    private String assessmentType;

    //评估结果（加密存储）
    private String result;

    //创建时间
    private LocalDateTime createdAt;

}
