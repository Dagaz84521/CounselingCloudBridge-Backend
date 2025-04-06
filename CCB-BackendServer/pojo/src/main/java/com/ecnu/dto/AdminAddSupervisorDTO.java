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
public class AdminAddSupervisorDTO implements Serializable {
    private String phoneNumber;

    private String realName;

    private String gender;

    private Integer age;

    private String avatarUrl;
}
