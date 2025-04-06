package com.ecnu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO implements Serializable {

    private String dayOfWeek;

    private Long counselorId;

    private String UserType;

    private String realName;

    private String avatarUrl;
}
