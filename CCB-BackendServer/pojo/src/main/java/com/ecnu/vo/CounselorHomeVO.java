package com.ecnu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselorHomeVO {

    private CounselorInfo counselorInfo;

    private List<LocalDate> schedule;

    private List<RecentSession> recentSessions;

    private List<Session> sessionList;

}
