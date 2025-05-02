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
public class ClientCounselorDTO implements Serializable {

    private String name;

    private String sortord;

    private Integer isFree;

    private Integer page;

    private Integer pagesize;

    private String dayOfWeek;
}
