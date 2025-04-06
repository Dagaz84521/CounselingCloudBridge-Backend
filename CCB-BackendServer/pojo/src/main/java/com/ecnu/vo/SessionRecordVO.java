package com.ecnu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionRecordVO implements Serializable {

    private Long sessionId;

    private Long senderId;

    private String content;

    private LocalDateTime createdAt;
}
