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
public class UserRegisterDTO implements Serializable {

    private String phoneNumber;

    private String realName;

    private String passwordHash;

    private Integer age;

    private String gender;

    private String occupation;

    private String avatarUrl;

    private String code;
}
