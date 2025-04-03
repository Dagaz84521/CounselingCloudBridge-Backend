package com.ecnu.service.impl;

import com.ecnu.constant.RequestStatusConstant;
import com.ecnu.context.BaseContext;
import com.ecnu.dto.OnlineCounselorDTO;
import com.ecnu.dto.SupervisorTodayRequestDTO;
import com.ecnu.entity.SupervisionRequest;
import com.ecnu.entity.User;
import com.ecnu.mapper.*;
import com.ecnu.service.SupervisorService;
import com.ecnu.vo.OnlineCounselor;
import com.ecnu.vo.RecentRequest;
import com.ecnu.vo.Request;
import com.ecnu.vo.SupervisorInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private RelationMapper relationMapper;

    public SupervisorInfo getSupervisorInfo() {
        User user = userMapper.geById(BaseContext.getCurrentId());
        SupervisorTodayRequestDTO supervisorTodayRequestDTO = requestMapper.getSupervisorTodayRequest(BaseContext.getCurrentId(), RequestStatusConstant.COMPLETED);
        SupervisorInfo supervisorInfo = new SupervisorInfo().builder()
                .realName(user.getRealName())
                .avatarUrl(user.getAvatarUrl())
                .totalRequests(requestMapper.getTotalRequests(BaseContext.getCurrentId(), RequestStatusConstant.COMPLETED))
                .todayRequests(supervisorTodayRequestDTO.getTodayRequests())
                .todayHours(supervisorTodayRequestDTO.getTodayHours())
                .build();
        return supervisorInfo;
    }

    public List<LocalDate> getSchedule() {
        List<LocalDate> schedule = scheduleMapper.getSchedule(BaseContext.getCurrentId());
        return schedule;
    }

    public List<RecentRequest> getRecentRequests() {
        List<RecentRequest> recentRequests = requestMapper.getRecentRequests(BaseContext.getCurrentId(), RequestStatusConstant.COMPLETED);
        return recentRequests;
    }

    public List<Request> getRequestList() {
        List<SupervisionRequest> requests = requestMapper.getRequestList(BaseContext.getCurrentId(), RequestStatusConstant.ACCEPTED);
        List<Request> requestList = new ArrayList<>();
        for (SupervisionRequest supervisionRequest : requests) {
            Request request = new Request();
            BeanUtils.copyProperties(supervisionRequest, request);
            User user = userMapper.geById(supervisionRequest.getCounselorId());
            request.setRealName(user.getRealName());
            requestList.add(request);
        }
        return requestList;
    }

    public List<OnlineCounselor> getOnlineCounselor(OnlineCounselorDTO onlineCounselorDTO) {
        PageHelper.startPage(onlineCounselorDTO.getPage(), onlineCounselorDTO.getPagesize());
        Page<OnlineCounselor> page = relationMapper.getOnlineCounselor(BaseContext.getCurrentId());
        return page.getResult();
    }
}
