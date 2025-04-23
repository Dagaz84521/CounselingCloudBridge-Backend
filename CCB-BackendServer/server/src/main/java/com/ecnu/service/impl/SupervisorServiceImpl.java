package com.ecnu.service.impl;

import com.ecnu.constant.RequestStatusConstant;
import com.ecnu.context.BaseContext;
import com.ecnu.dto.CounselorHistoryDTO;
import com.ecnu.dto.OnlineCounselorDTO;
import com.ecnu.dto.SupervisorTodayRequestDTO;
import com.ecnu.entity.SupervisionRequest;
import com.ecnu.entity.User;
import com.ecnu.exception.IllegalRequestIDException;
import com.ecnu.mapper.*;
import com.ecnu.service.SupervisorService;
import com.ecnu.vo.*;
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
        User user = userMapper.getById(BaseContext.getCurrentId());
        SupervisorTodayRequestDTO supervisorTodayRequestDTO = requestMapper.getSupervisorTodayRequest(BaseContext.getCurrentId(), RequestStatusConstant.COMPLETED);
        long todayRequests = 0;
        String todayHours = "00:00:00";
        if(supervisorTodayRequestDTO != null) {
            todayRequests = supervisorTodayRequestDTO.getTodayRequests();
            Long seconds = supervisorTodayRequestDTO.getTodayHours();
            if(seconds != null) {
                Long hours = seconds / 3600;
                Long remainder = seconds % 3600;
                Long minutes = remainder / 60;
                seconds = remainder % 60;
                todayHours = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }
        }
        SupervisorInfo supervisorInfo = new SupervisorInfo().builder()
                .realName(user.getRealName())
                .avatarUrl(user.getAvatarUrl())
                .totalRequests(requestMapper.getTotalRequests(BaseContext.getCurrentId(), RequestStatusConstant.COMPLETED))
                .todayRequests(todayRequests)
                .todayHours(todayHours)
                .build();
        return supervisorInfo;
    }

    public List<String> getSchedule() {
        List<String> schedule = scheduleMapper.getSchedule(BaseContext.getCurrentId());
        return schedule;
    }

    public List<RecentRequest> getRecentRequests() {
        List<RecentRequest> recentRequests = requestMapper.getRecentRequests(BaseContext.getCurrentId(), RequestStatusConstant.COMPLETED);
        for (RecentRequest recentRequest : recentRequests) {
            Long seconds = Long.parseLong(recentRequest.getDuration());
            Long hours = seconds / 3600;
            Long remainder = seconds % 3600;
            Long minutes = remainder / 60;
            seconds = remainder % 60;
            String duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            recentRequest.setDuration(duration);
        }
        return recentRequests;
    }

    public List<Request> getRequestList() {
        List<SupervisionRequest> requests = requestMapper.getRequestList(BaseContext.getCurrentId(), null);
        List<Request> requestList = new ArrayList<>();
        for (SupervisionRequest supervisionRequest : requests) {
            Request request = new Request();
            BeanUtils.copyProperties(supervisionRequest, request);
            User user = userMapper.getById(supervisionRequest.getCounselorId());
            request.setRealName(user.getRealName());
            requestList.add(request);
        }
        return requestList;
    }

    public OnlineCounselorVO getOnlineCounselor(OnlineCounselorDTO onlineCounselorDTO) {
        PageHelper.startPage(onlineCounselorDTO.getPage(), onlineCounselorDTO.getPagesize());
        Page<OnlineCounselor> page = relationMapper.getOnlineCounselor(BaseContext.getCurrentId());

        OnlineCounselorVO onlineCounselorVO = OnlineCounselorVO.builder()
                .onlineCounselors(page.getResult())
                .total(page.getTotal())
                .build();

        return onlineCounselorVO;
    }

    public SupervisorHistoryVO getHistory(CounselorHistoryDTO counselorHistoryDTO) {
        PageHelper.startPage(counselorHistoryDTO.getPage(), counselorHistoryDTO.getPagesize());
        Page<RecentRequest> page = requestMapper.getHistory(counselorHistoryDTO, BaseContext.getCurrentId());
        List<RecentRequest> requests = page.getResult();
        for (RecentRequest request : requests) {
            Long seconds = Long.parseLong(request.getDuration());
            Long hours = seconds / 3600;
            Long remainder = seconds % 3600;
            Long minutes = remainder / 60;
            seconds = remainder % 60;
            String duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            request.setDuration(duration);
        }
        return new SupervisorHistoryVO(requests, page.getTotal());
    }

    @Override
    public Long acceptRequest(Long supervisorId, Long counselorId) {
        SupervisionRequest supervisionRequest = requestMapper.getPendingByParticipationId(supervisorId, counselorId);
        if (supervisionRequest == null) {
            return null;
        }
        supervisionRequest.setStatus(RequestStatusConstant.ACCEPTED);
        requestMapper.update(supervisionRequest);
        return supervisionRequest.getRequestId();
    }

    @Override
    public void endRequest(Long requestId) throws IllegalRequestIDException {
        SupervisionRequest supervisionRequest = requestMapper.getById(requestId);
        if (supervisionRequest == null) {
            throw new IllegalRequestIDException("非法requestId: " + requestId );
        }
        supervisionRequest.setStatus(RequestStatusConstant.COMPLETED);
        requestMapper.update(supervisionRequest);
    }
}
