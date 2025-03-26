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
public class RecentSession {

    private Long sessionId;

    private Long clientId;

    private String realName;

    private LocalDateTime duration;

    private LocalDateTime startTime;

    private Integer rating;

}
