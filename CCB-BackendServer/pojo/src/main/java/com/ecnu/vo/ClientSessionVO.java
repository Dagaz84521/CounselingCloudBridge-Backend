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
public class ClientSessionVO implements Serializable {
    private Long counselorId;

    private String realName;

    private String avatarUrl;

    private Long sessionId;
}
