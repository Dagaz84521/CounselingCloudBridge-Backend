package com.ecnu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentSession implements Serializable {

    private Long sessionId;

    private Long clientId;

    private String realName;

    private String duration;

    private LocalDateTime startTime;

    private Integer rating;

}
