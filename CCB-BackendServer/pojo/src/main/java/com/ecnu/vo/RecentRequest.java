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
public class RecentRequest implements Serializable {

    private Long requestId;

    private Long counselorId;

    private String realName;

    private LocalDateTime duration;

    private LocalDateTime startTime;
}
