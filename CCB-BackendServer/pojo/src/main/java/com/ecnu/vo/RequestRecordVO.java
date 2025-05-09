package com.ecnu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRecordVO {

    private Long requestId;

    private Long senderId;

    private String senderName;

    private String type;

    private String content;

    private LocalDateTime createdAt;
}
