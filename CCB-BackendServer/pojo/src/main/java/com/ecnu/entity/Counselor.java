package com.ecnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * 咨询师信息表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Counselor implements Serializable {

    //咨询师唯一标识
    private Long counselorId;

    //资质证书编号
    private String certification;

    //专长领域（如焦虑、抑郁等）
    private String expertise;

    //咨询师综合评分
    private Float rating;

    //最大同时会话数
    private Integer maxSessions;

    //当前会话数
    private Integer currentSessions;

    //咨询师解决过的咨询总数
    private Integer totalSessions;

    //工作年数
    private Integer yearsExperience;

    //个人简介
    private String bio;
}
