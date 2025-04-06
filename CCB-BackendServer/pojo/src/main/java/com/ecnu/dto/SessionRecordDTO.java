package com.ecnu.dto;

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
public class SessionRecordDTO implements Serializable {

    private Long sessionId;

    private Long senderId;

    private Long receiverId;

    private String content;

    private LocalDateTime createTime;
}
