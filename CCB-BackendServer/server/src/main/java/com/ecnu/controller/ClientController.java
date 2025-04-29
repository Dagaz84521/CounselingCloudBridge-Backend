package com.ecnu.controller;

import com.ecnu.dto.ClientCounselorDTO;
import com.ecnu.entity.Session;
import com.ecnu.result.Result;
import com.ecnu.service.ClientService;
import com.ecnu.service.SessionRecordService;
import com.ecnu.service.SessionsService;
import com.ecnu.vo.ClientCounselorDetailVO;
import com.ecnu.vo.ClientHomeVO;
import com.ecnu.vo.ClientSessionVO;
import com.ecnu.vo.SessionRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@Slf4j
@Api(tags = "客户相关接口")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private SessionsService sessionsService;

    @Autowired
    private SessionRecordService sessionRecordService;



    @GetMapping("/home")
    @ApiOperation(value = "客户首页")
    public Result<List<ClientHomeVO>> getHomeInfo() {
        log.info("客户首页");

        List<ClientHomeVO> counselorList = clientService.getHomeInfo();

        return Result.success(counselorList);
    }

    /**
     * 查看咨询师排班页面
     * @param clientCounselorDTO
     * @return
     */
    @GetMapping("/counselor")
    @ApiOperation(value = "查看咨询师排班页面")
    public Result<List<ClientHomeVO>> getCounselorScheduled(ClientCounselorDTO clientCounselorDTO) {
        log.info("查看咨询师排班页面");

        List<ClientHomeVO> counselorList = clientService.getCounselorScheduled(clientCounselorDTO);

        return Result.success(counselorList);
    }


    @GetMapping("/session")
    @ApiOperation(value = "客户咨询页面")
    public Result<ClientSessionVO> getSession() {
        log.info("客户咨询页面");

        ClientSessionVO clientSessionVO = clientService.getSession();

        return Result.success(clientSessionVO);
    }


    @GetMapping("/session/history")
    @ApiOperation(value = "客户咨询页面")
    public Result<List<SessionRecordVO>> getSession(@RequestParam("sessionId") Long sessionId) {

        List<SessionRecordVO> record = sessionRecordService.getHistoryMessages(sessionId, 0L, 0L);

        return Result.success(record);
    }


    /**
     * 客户开始一个新的咨询
     * @param
     * @return
     */
    @PostMapping("/session/add")
    @ApiOperation(value = "客户开始会话")
    public Result<Long> startSession(
            @RequestParam("clientId") Long clientId,
            @RequestParam("counselorId") Long counselorId) {
        log.info("客户开始一个新的咨询");
        Session session = sessionsService.startSession(clientId, counselorId);
        return Result.success(session.getSessionId());
    }


    /**
     * 客户结束一个咨询
     * @param
     * @return
     */
    @PostMapping("/session/end")
    @ApiOperation(value = "客户结束会话")
    public Result<Long> endSession(
            @RequestParam("sessionId") Long sessionId,
            @RequestParam("rating") Integer rating) {
        log.info("客户结束一个新的咨询");
        sessionsService.endSession(sessionId,rating);

        return Result.success();
    }

    /**
     * 获取当前用户对应的活跃会话
     * @param
     * @return
     */
    @PostMapping("/session/get")
    @ApiOperation(value = "获取当前用户对应的活跃会话")
    public Result<List<Long>> getRelatedSessions(
            @RequestParam("userId") Long userId) {
        log.info("客户 {} 获取会话", userId);
        List<Long> sessionIds = sessionsService.getRelatedSession(userId);
        return Result.success(sessionIds);
    }

    @GetMapping("/counselor/{counselorId}")
    @ApiOperation(value = "查看咨询师详情")
    public Result<ClientCounselorDetailVO> getCounselorDetail(@PathVariable("counselorId") Long counselorId) {
        log.info("查看咨询师详情");

        ClientCounselorDetailVO clientCounselorDetailVO = clientService.getCounselorDetailById(counselorId);

        return Result.success(clientCounselorDetailVO);
    }
}
