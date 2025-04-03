package com.ecnu.service;

import com.ecnu.dto.OnlineCounselorDTO;
import com.ecnu.vo.OnlineCounselor;
import com.ecnu.vo.RecentRequest;
import com.ecnu.vo.Request;
import com.ecnu.vo.SupervisorInfo;

import java.time.LocalDate;
import java.util.List;

public interface SupervisorService {
    
    SupervisorInfo getSupervisorInfo();

    List<LocalDate> getSchedule();

    List<RecentRequest> getRecentRequests();

    List<Request> getRequestList();

    List<OnlineCounselor> getOnlineCounselor(OnlineCounselorDTO onlineCounselorDTO);
}
