package com.ecnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * 会话表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session implements Serializable {

    //会话唯一标识
    private Long sessionId;

    //求助者用户ID
    private Long clientId;

    //咨询师用户ID
    private Long counselorId;

    //会话状态：待开始、进行中、已结束
    private String status;

    //开始时间
    private LocalDateTime startTime;

    //结束时间
    private LocalDateTime endTime;

    //评分
    private Integer rating;

    //咨询类型
    private String type;

    //咨询师建议
    private String advice;

    //创建时间
    private LocalDateTime createdAt;

}
