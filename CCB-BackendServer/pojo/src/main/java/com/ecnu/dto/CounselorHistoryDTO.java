package com.ecnu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselorHistoryDTO implements Serializable {

    private String name;

    private LocalDate date;

    private Integer page;

    private Integer pagesize;
}
