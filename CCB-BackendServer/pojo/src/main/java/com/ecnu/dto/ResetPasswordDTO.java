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
public class ResetPasswordDTO implements Serializable {

    private  String phoneNumber;

    private  String code;

    private  String passwordHash;
}
