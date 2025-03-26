package com.ecnu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    private String phoneNumber;

    private String realName;

    private String password;

    private Integer age;

    private String gender;

    private String occupation;

    private String avatarUrl;
}
