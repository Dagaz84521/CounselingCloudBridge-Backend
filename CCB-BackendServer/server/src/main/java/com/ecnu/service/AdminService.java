package com.ecnu.service;

import com.ecnu.dto.*;
import com.ecnu.vo.*;

import java.util.List;

public interface AdminService {

    AdminHomeVO getHomeInfo();

    List<OnlineCounselor> getOnlineCounselor(OnlineCounselorDTO onlineCounselorDTO);

    List<ScheduleVO> getSchedule();

    ScheduleOfDayVO getScheduleOfDay(String day);

    List<AdminCounselorVO> getCounselorList(AdminCounselorDTO adminCounselorDTO);

    void updateCounselor(AdminUpdateCounselorDTO adminUpdateCounselorDTO);

    void addCounselor(AdminAddCounselorDTO adminAddCounselorDTO);

    List<AdminSupervisorVO> getSupervisorList(AdminCounselorDTO adminCounselorDTO);

    void updateSupervisor(AdminUpdateSupervisorDTO adminUpdateSupervisorDTO);

    void addSupervisor(AdminAddSupervisorDTO adminAddSupervisorDTO);
}
