package com.ecnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRecord {
    //记录唯一标识
    private Long recordId;

    //关联会话ID
    private Long requestId;

    //发送者ID
    private Long senderId;

    //接收者ID
    private Long receiverId;

    //会话内容（加密存储）
    private String content;

    //创建时间
    private LocalDateTime createdAt;
}
