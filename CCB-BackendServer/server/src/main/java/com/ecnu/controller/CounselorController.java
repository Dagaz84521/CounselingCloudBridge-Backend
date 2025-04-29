package com.ecnu.controller;

import com.ecnu.constant.PageConstant;
import com.ecnu.context.BaseContext;
import com.ecnu.dto.CounselorHistoryDTO;
import com.ecnu.dto.SessionAddAdviceDTO;
import com.ecnu.result.Result;
import com.ecnu.service.CounselorService;
import com.ecnu.service.SessionRecordService;
import com.ecnu.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counselor")
@Api(tags = "咨询师相关接口")
@Slf4j
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    @Autowired
    private SessionRecordService sessionRecordService;

    /**
     * 咨询师首页
     * @return
     */
    @GetMapping("/home")
    @ApiOperation(value = "咨询师首页")
    public Result<CounselorHomeVO> getHomeInfo() {
        CounselorInfo counselorInfo = counselorService.getCounselorInfo();
        List<String> schedule = counselorService.getSchedule();
        List<RecentSession> recentSessions = counselorService.getRecentSessions();
        List<Session> sessionList = counselorService.getSessionList();
        List<Long> supervisorIds = counselorService.getSupervisorIds(BaseContext.getCurrentId());
        CounselorHomeVO counselorHomeVO = new CounselorHomeVO().builder()
                .counselorInfo(counselorInfo)
                .schedule(schedule)
                .recentSessions(recentSessions)
                .sessionList(sessionList)
                .supervisorIds(supervisorIds)
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
    public Result<CounselorHistoryVO> getHistory(CounselorHistoryDTO counselorHistoryDTO) {
        log.info("咨询历史记录:{}", counselorHistoryDTO);
        CounselorHistoryVO counselorHistoryVO = counselorService.getHistory(counselorHistoryDTO);
        return Result.success(counselorHistoryVO);
    }

    /**
     * 会话历史聊天记录
     * @param sessionId
     * @return
     */
    @GetMapping("/session/history")
    @ApiOperation(value = "获取具体某个会话的历史记录")
    public Result<List<SessionRecordVO>> getHistory(@RequestParam("sessionId") Long sessionId) {
        List<SessionRecordVO> record = sessionRecordService.getHistoryMessages(sessionId, 0L, 0L);
        return Result.success(record);
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
        CounselorSessionVO counselorSessionVO = counselorService.getSession(sessionid, clientid);
        List<SessionRecordVO> records = sessionRecordService.getHistoryMessages(sessionid, 0L, 0L);
        counselorSessionVO.setHistory(records);
        return Result.success(counselorSessionVO);
    }

    /**
     * 咨询师添加咨询评价
     * @param sessionAddAdviceDTO
     * @return
     */
    @PutMapping("/session")
    @ApiOperation(value = "咨询师添加咨询评价")
    public Result addSessionAdvice(@RequestBody SessionAddAdviceDTO sessionAddAdviceDTO) {
        log.info("咨询师添加咨询评价:{}", sessionAddAdviceDTO);
        counselorService.addSessionAdvice(sessionAddAdviceDTO);
        return Result.success();
    }

    /**
     * 获取咨询师详细信息 (通常由用户或管理员查看)
     * @param counselorId 咨询师ID
     * @return 咨询师详细信息
     */
    @GetMapping("/detail")
    @ApiOperation(value = "获取咨询师详细信息")
    public Result<CounselorDetailVO> getCounselorDetail(
            @ApiParam(name = "counselor_id", value = "咨询师的用户ID", required = true, example = "1")
            @RequestParam("counselorId") Long counselorId) {

        log.info("请求获取咨询师详细信息，ID: {}", counselorId);

        CounselorDetailVO counselorDetail = counselorService.getCounselorDetailById(counselorId);

        if (counselorDetail == null) {
            return Result.error("未找到指定的咨询师");
        }

        return Result.success(counselorDetail);
    }


    @GetMapping("/updateBio")
    @ApiOperation(value = "获取咨询师简介")
    public Result<String> getBio() {
        return Result.success(counselorService.getBio());
    }

    @PutMapping("/updateBio")
    @ApiOperation(value = "更新咨询师简介")
    public Result updateBio(@RequestBody String bio) {
        counselorService.updateBio(bio);
        return Result.success();
    }
    /**
     * 咨询师向督导发起求助
     * @param supervisorId 督导ID
     * @return 咨询师详细信息
     * */
    @PostMapping("/request/start/{supervisorId}")
    @ApiOperation(value = "咨询师向督导发起求助")
    public Result<RequestStartVO> startRequest(@PathVariable Long supervisorId, @RequestParam Long sessionId) {
        Long requestId = counselorService.addRequest(supervisorId);
        RequestStartVO requestStartVO = RequestStartVO.builder().requestId(requestId).sessionId(sessionId).build();
        return Result.success(requestStartVO);
    }

    /**
     * 咨询师主动结束求助
     * @param requestId 需要结束求助的ID
     * @return 咨询师详细信息
     * */
    @PostMapping("/request/end/{requestId}")
    @ApiOperation(value = "咨询师主动结束求助")
    public Result<Long> endRequest(@PathVariable Long requestId) {
        counselorService.endRequest(requestId);
        return Result.success();
    }
}
