package com.ecnu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCounselorVO implements Serializable {
    private Long counselorId;

    private String counselorName;

    private Long supervisorId;

    private String supervisorName;

    private Long totalSessions;

    private LocalDateTime totalHours;

    private Integer rating;

    private List<String> schedule;
}
