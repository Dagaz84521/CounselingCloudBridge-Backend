package com.ecnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * 会话记录表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionRecord {

    //记录唯一标识
    private Long recordId;

    //关联会话ID
    private Long sessionId;

    //会话内容（加密存储）
    private String content;

    //匿名化数据（用于科研）
    private String anonymizedData;

    //创建时间
    private LocalDateTime createdAt;

}
