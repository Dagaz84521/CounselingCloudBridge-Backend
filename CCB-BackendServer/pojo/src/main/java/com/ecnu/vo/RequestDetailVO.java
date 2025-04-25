package com.ecnu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailVO {

    private String realName;

    private String phoneNumber;

    private String avatarUrl;

    private LocalDateTime startTime;

    private List<RequestRecordVO> records;
}
