package com.ecnu.controller;

import com.ecnu.context.BaseContext;
import com.ecnu.dto.CounselorHistoryDTO;
import com.ecnu.dto.OnlineCounselorDTO;
import com.ecnu.exception.IllegalRequestIDException;
import com.ecnu.mapper.RequestMapper;
import com.ecnu.mapper.UserMapper;
import com.ecnu.result.Result;
import com.ecnu.service.RequestRecordService;
import com.ecnu.service.SupervisorService;
import com.ecnu.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supervisor")
@Api(tags = "督导相关接口")
@Slf4j
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private RequestRecordService requestRecordService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RequestMapper requestMapper;

    @GetMapping("/home")
    @ApiOperation(value = "督导首页")
    public Result<SupervisorHomeVO> getHomeInfo() {
        SupervisorInfo supervisorInfo = supervisorService.getSupervisorInfo();
        List<String> schedule = supervisorService.getSchedule();
        List<RecentRequest> recentRequests = supervisorService.getRecentRequests();
        List<Request> requestList = supervisorService.getRequestList();
        SupervisorHomeVO supervisorHomeVO = new SupervisorHomeVO().builder()
                .supervisorInfo(supervisorInfo)
                .schedule(schedule)
                .recentRequests(recentRequests)
                .requestList(requestList)
                .build();
        return Result.success(supervisorHomeVO);
    }

    @GetMapping("onlinecounselor")
    @ApiOperation(value = "在线咨询师列表")
    public Result<OnlineCounselorVO> getOnlineCounselor(OnlineCounselorDTO onlineCounselorDTO) {

        OnlineCounselorVO onlineCounselorVO = supervisorService.getOnlineCounselor(onlineCounselorDTO);

        return Result.success(onlineCounselorVO);
    }

    @GetMapping("/history")
    @ApiOperation(value = "督导求助历史记录")
    public Result<SupervisorHistoryVO> getHistory(CounselorHistoryDTO counselorHistoryDTO) {
        SupervisorHistoryVO supervisorHistoryVO = supervisorService.getHistory(counselorHistoryDTO);
        return Result.success(supervisorHistoryVO);
    }

    /**
     * 求助历史聊天记录
     * @param requestId
     * @return
     */
    @GetMapping("/request/history")
    @ApiOperation(value = "获取具体某个会话的历史记录")
    public Result<ChatPageVO> getHistory(@RequestParam(name = "requestId", required = false) Long requestId, @RequestParam(name = "sessionId", required = false) Long sessionId) {
        RequestDetailVO requestDetailVO = null;
        CounselorSessionVO counselorSessionVO = null;
        if (requestId != null) {
            requestDetailVO = supervisorService.getRequest(requestId);
        }

        if (sessionId != null) {
            counselorSessionVO = supervisorService.getCounselorSession(sessionId);
        }

        ChatPageVO supervisorRequestPageVO = ChatPageVO.builder()
                .sessionData(counselorSessionVO)
                .requestData(requestDetailVO)
                .build();
        return Result.success(supervisorRequestPageVO);
    }

    @PostMapping("/request/accept/{counselorId}")
    public Result<Long> acceptRequest(@PathVariable("counselorId") Long counselorId) {
        Long requestId = supervisorService.acceptRequest(BaseContext.getCurrentId(), counselorId);
        if (requestId == null) {
            return Result.error("无对应CounselorID: " + counselorId);
        }
        return Result.success(requestId);
    }

    @PostMapping("/request/end/{requestId}")
    public Result<Object> endRequest(@PathVariable("requestId") Long requestId) {
        try {
            supervisorService.endRequest(requestId);
        } catch (IllegalRequestIDException e) {
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

}
