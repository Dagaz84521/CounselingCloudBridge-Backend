package com.ecnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * 权限表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class permission implements Serializable {

    //权限唯一标识
    private Long permissionId;

    //用户类型
    private String userType;

    //资源（如“会话记录”）
    private String resource;

    //访问级别：读、写、无
    private String accessLevel;

}
