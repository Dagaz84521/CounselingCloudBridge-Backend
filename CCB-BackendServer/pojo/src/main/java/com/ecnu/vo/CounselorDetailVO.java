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
public class CounselorDetailVO implements Serializable {

    private String name;

    private String expertise;

    private String certification;

    private Float rating;

    private Integer totalSessions;

    private Integer yearsExperience;

    private String bio;
}
