package com.ecnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * 咨询师排班表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule implements Serializable {

    //排班唯一标识
    private Long scheduleId;

    //咨询师用户ID
    private Long counselorId;

    //排班日期
    private String dayOfWeek;

    //排班状态：激活、未激活
    private String status;

    //创建时间
    private LocalDateTime createdAt;

    //更新时间
    private LocalDateTime updatedAt;

}
