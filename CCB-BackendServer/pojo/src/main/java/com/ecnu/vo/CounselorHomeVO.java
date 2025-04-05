package com.ecnu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselorHomeVO implements Serializable {

    private CounselorInfo counselorInfo;

    private List<String> schedule;

    private List<RecentSession> recentSessions;

    private List<Session> sessionList;

}
