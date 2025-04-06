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
public class AdminHomeVO implements Serializable {
    
    private String realName;
    
    private String avatarUrl;
    
    private Long todaySessions;
    
    private String todayHours;
    
    private Long currentSessions;

    private Long currentRequests;
}
