package com.ecnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * 用户表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    //用户唯一标识
    private Long userId;

    //手机号
    private String phoneNumber;

    //真实名字
    private String realName;

    //密码加密值
    private String passwordHash;

    //年龄
    private Integer age;

    //性别
    private String gender;

    //职业
    private String occupation;

    //用户类型：求助者、咨询师、管理员、督导
    private String userType;

    //用户状态：激活、未激活、禁用
    private String status;

    //用户头像URL
    private String avatarUrl;

    //创建时间
    private LocalDateTime createdAt;

    //更新时间
    private LocalDateTime updatedAt;

}
