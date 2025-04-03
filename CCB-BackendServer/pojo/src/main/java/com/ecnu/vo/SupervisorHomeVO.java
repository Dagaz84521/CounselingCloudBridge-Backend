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
public class SupervisorHomeVO implements Serializable {

    private SupervisorInfo supervisorInfo;

    private List<LocalDate> schedule;

    private List<RecentRequest> recentRequests;

    private List<Request> requestList;

}
