package com.ecnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * 咨询师与督导之间的关系表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselorSupervisorRelation {

    //关系唯一标识
    private Long relationId;

    //咨询师用户ID
    private Long counselorId;

    //督导用户ID
    private Long supervisorId;

    //关系状态：激活、未激活
    private String status;

    //创建时间
    private LocalDateTime createdAt;

    //更新时间
    private LocalDateTime updatedAt;

}
