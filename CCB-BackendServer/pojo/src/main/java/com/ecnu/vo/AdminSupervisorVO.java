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
public class AdminSupervisorVO implements Serializable {
    private Long supervisorId;

    private String realName;

    private Long totalRequests;

    private LocalDateTime totalHours;

    private List<String> schedule;
}
