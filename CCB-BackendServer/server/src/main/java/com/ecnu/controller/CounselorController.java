package com.ecnu.controller;

import com.ecnu.dto.CounselorHistoryDTO;
import com.ecnu.dto.SessionAddAdviceDTO;
import com.ecnu.result.Result;
import com.ecnu.service.CounselorService;
import com.ecnu.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/counselor")
@Api(tags = "咨询师相关接口")
@Slf4j
public class CounselorController {

    @Autowired
    private CounselorService CounselorService;


    /**
     * 咨询师首页
     * @return
     */
    @GetMapping("/home")
    @ApiOperation(value = "咨询师首页")
    public Result<CounselorHomeVO> getHomeInfo() {
        CounselorInfo counselorInfo = CounselorService.getCounselorInfo();
        List<LocalDate> schedule = CounselorService.getSchedule();
        List<RecentSession> recentSessions = CounselorService.getRecentSessions();
        List<Session> sessionList = CounselorService.getSessionList();
        CounselorHomeVO counselorHomeVO = new CounselorHomeVO().builder()
                .counselorInfo(counselorInfo)
                .schedule(schedule)
                .recentSessions(recentSessions)
                .sessionList(sessionList)
                .build();
        return Result.success(counselorHomeVO);
    }

    /**
     * 咨询历史记录
     * @param counselorHistoryDTO
     * @return
     */
    @GetMapping("/history")
    @ApiOperation(value = "咨询历史记录")
    public Result<List<RecentSession>> getHistory(CounselorHistoryDTO counselorHistoryDTO) {
        log.info("咨询历史记录:{}", counselorHistoryDTO);
        List<RecentSession> sessions = CounselorService.getHistory(counselorHistoryDTO);
        return Result.success(sessions);
    }

    /**
     * 咨询师咨询页面
     * @param sessionid
     * @param clientid
     * @return
     */
    @GetMapping("/session/{sessionid}/{clientid}")
    @ApiOperation(value = "咨询师咨询页面")
    public Result<CounselorSessionVO> getSession(@PathVariable Long sessionid, @PathVariable Long clientid) {
        CounselorSessionVO counselorSessionVO = CounselorService.getSession(sessionid, clientid);
        return Result.success(counselorSessionVO);
    }

    /**
     * 咨询师添加咨询评价
     * @param sessionAddAdviceDTO
     * @return
     */
    @PutMapping("/session")
    @ApiOperation(value = "咨询师添加咨询评价")
    public Result addSessionAdvice(@RequestBody SessionAddAdviceDTO sessionAddAdviceDTO, @PathVariable Long sessionid) {
        log.info("咨询师添加咨询评价:{}", sessionAddAdviceDTO);
        CounselorService.addSessionAdvice(sessionAddAdviceDTO, sessionid);
        return Result.success();
    }
}
