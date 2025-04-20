package com.ecnu.controller;

import com.ecnu.context.BaseContext;
import com.ecnu.dto.CounselorHistoryDTO;
import com.ecnu.dto.OnlineCounselorDTO;
import com.ecnu.exception.IllegalRequestIDException;
import com.ecnu.result.Result;
import com.ecnu.service.SupervisorService;
import com.ecnu.utils.SmsUtil;
import com.ecnu.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/supervisor")
@Api(tags = "督导相关接口")
@Slf4j
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

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
        List<OnlineCounselor> onlineCounselorList = supervisorService.getOnlineCounselor(onlineCounselorDTO);

        OnlineCounselorVO onlineCounselorVO = OnlineCounselorVO.builder()
                .onlineCounselors(onlineCounselorList)
                .total(onlineCounselorList.size())
                .build();

        return Result.success(onlineCounselorVO);
    }

    @GetMapping("/history")
    @ApiOperation(value = "督导求助历史记录")
    public Result<SupervisorHistoryVO> getHistory(CounselorHistoryDTO counselorHistoryDTO) {
        SupervisorHistoryVO supervisorHistoryVO = supervisorService.getHistory(counselorHistoryDTO);
        return Result.success(supervisorHistoryVO);
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
