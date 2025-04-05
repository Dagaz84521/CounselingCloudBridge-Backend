package com.ecnu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateCounselorDTO implements Serializable {
    private Long counselorId;

    private String realName;

    private Long supervisorId;

    private List<String> schedule;
}
