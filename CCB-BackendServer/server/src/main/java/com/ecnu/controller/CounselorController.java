package com.ecnu.controller;

import com.ecnu.result.Result;
import com.ecnu.service.CounselorService;
import com.ecnu.vo.CounselorHomeVO;
import com.ecnu.vo.CounselorInfo;
import com.ecnu.vo.RecentSession;
import com.ecnu.vo.Session;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
