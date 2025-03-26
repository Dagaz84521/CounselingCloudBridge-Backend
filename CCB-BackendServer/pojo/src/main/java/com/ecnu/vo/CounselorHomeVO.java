package com.ecnu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselorHomeVO {

    private CounselorInfo counselorInfo;

    private RecentSession[] recentSessions;

    private Schedule schedule;

    private Session[] sessionList;

}
