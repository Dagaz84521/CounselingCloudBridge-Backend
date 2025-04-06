package com.ecnu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSupervisor implements Serializable {
    private Long supervisorId;

    private String realName;

    private String avatarUrl;
}
