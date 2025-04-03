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
public class SupervisorInfo {

    private String realName;

    private String avatarUrl;

    private Long totalRequests;

    private Long todayRequests;

    private LocalDateTime todayHours;

}
