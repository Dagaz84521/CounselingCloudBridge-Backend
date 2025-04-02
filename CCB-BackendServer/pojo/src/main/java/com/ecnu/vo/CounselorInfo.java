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
public class CounselorInfo implements Serializable {

    private String realName;

    private String avatarUrl;

    private Integer totalSessions;

    private Integer todaySessions;

    private LocalDateTime todayHours;

    private  Integer currentSessions;
}
