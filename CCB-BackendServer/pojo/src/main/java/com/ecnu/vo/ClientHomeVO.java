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
public class ClientHomeVO implements Serializable {

    private Long counselorId;

    private String realName;

    private String avatarUrl;

    private String expertise;

    private Integer rating;

    private Boolean isFree;
}
