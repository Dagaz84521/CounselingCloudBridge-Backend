package com.ecnu.controller;

import com.ecnu.dto.*;
import com.ecnu.result.Result;
import com.ecnu.service.AdminService;
import com.ecnu.service.SessionRecordService;
import com.ecnu.service.SessionsService;
import com.ecnu.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Slf4j
@Api(tags = "管理员相关接口")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SessionRecordService sessionRecordService;

    @GetMapping("/home")
    @ApiOperation(value = "管理员首页")
    public Result<AdminHomeVO> getHomeInfo() {
        AdminHomeVO adminHomeVO = adminService.getHomeInfo();
        return Result.success(adminHomeVO);
    }

    @GetMapping("/onlinecounselor")
    @ApiOperation(value = "在线咨询师")
    public Result<OnlineCounselorVO> getOnlineCounselor(OnlineCounselorDTO onlineCounselorDTO) {

        OnlineCounselorVO onlineCounselorVO = adminService.getOnlineCounselor(onlineCounselorDTO);

        return Result.success(onlineCounselorVO);
    }

    @GetMapping("/onlinesupervisor")
    @ApiOperation(value = "在线督导")
    public Result<OnlineSupervisorVO> getOnlineSupervisor(OnlineCounselorDTO onlineCounselorDTO) {

        OnlineSupervisorVO onlineSupervisorVO = adminService.getOnlineSupervisor(onlineCounselorDTO);

        return Result.success(onlineSupervisorVO);
    }

    @GetMapping("/schedule")
    @ApiOperation(value = "排班页面")
    public Result<List<ScheduleVO>> getSchedule() {
        List<ScheduleVO> schedule = adminService.getSchedule();
        return Result.success(schedule);
    }

    @GetMapping("/schedule/{dateofweek}")
    @ApiOperation(value = "某一天排班")
    public Result<ScheduleOfDayVO> getScheduleOfDay(@PathVariable Integer dateofweek) {
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        ScheduleOfDayVO scheduleOfDayVO = adminService.getScheduleOfDay(days[dateofweek - 1]);
        return Result.success(scheduleOfDayVO);
    }

    @GetMapping("/counselor")
    @ApiOperation(value = "咨询师管理页面")
    public Result<AdminCounselorPageVO> getCounselorList(AdminCounselorDTO adminCounselorDTO) {
        AdminCounselorPageVO adminCounselorPageVO = adminService.getCounselorList(adminCounselorDTO);
        return Result.success(adminCounselorPageVO);
    }

    @PutMapping("/counselor")
    @ApiOperation(value = "修改咨询师信息")
    public Result updateCounselor(@RequestBody AdminUpdateCounselorDTO adminUpdateCounselorDTO) {
        adminService.updateCounselor(adminUpdateCounselorDTO);
        return Result.success();
    }

    @PostMapping("/counselor")
    @ApiOperation(value = "添加咨询师")
    public Result addCounselor(@RequestBody AdminAddCounselorDTO adminAddCounselorDTO) {
        adminService.addCounselor(adminAddCounselorDTO);
        return Result.success();
    }

    @GetMapping("/supervisor")
    @ApiOperation(value = "督导管理页面")
    public Result<AdminSupervisorPageVO> getSupervisorList(AdminCounselorDTO adminCounselorDTO) {
        AdminSupervisorPageVO adminSupervisorPageVO = adminService.getSupervisorList(adminCounselorDTO);
        return Result.success(adminSupervisorPageVO);
    }

    @PutMapping("/supervisor")
    @ApiOperation(value = "修改督导信息")
    public Result updateSupervisor(@RequestBody AdminUpdateSupervisorDTO adminUpdateSupervisorDTO) {
        adminService.updateSupervisor(adminUpdateSupervisorDTO);
        return Result.success();
    }

    @PostMapping("/supervisor")
    @ApiOperation(value = "添加督导")
    public Result addSupervisor(@RequestBody AdminAddSupervisorDTO adminAddSupervisorDTO) {
        adminService.addSupervisor(adminAddSupervisorDTO);
        return Result.success();
    }

    @GetMapping("/supervisorlist")
    @ApiOperation(value = "督导列表")
    public Result<List<SupervisorListVO>> getSupervisorList() {
        List<SupervisorListVO> supervisorList = adminService.supervisorList();
        return Result.success(supervisorList);
    }

    @GetMapping("/history")
    @ApiOperation(value = "管理员查询咨询师历史记录")
    public Result<CounselorHistoryVO> getHistory(CounselorHistoryDTO counselorHistoryDTO) {

        CounselorHistoryVO counselorHistoryVO = adminService.getHistory(counselorHistoryDTO);
        return Result.success(counselorHistoryVO);
    }


    @GetMapping("/session/history")
    @ApiOperation(value = "管理员查询咨询师具体会话中历史记录")
    public Result<List<SessionRecordVO>> getHistory(@RequestParam("sessionId") Long sessionId) {
        List<SessionRecordVO> record = sessionRecordService.getHistoryMessages(sessionId, 0L, 0L);
        return Result.success(record);
    }

    @GetMapping("/todaySessionVariation")
    @ApiOperation(value = "今日咨询数量变化")
    public Result<TodaySessionVariationVO> getTodaySessionVariation() {
        TodaySessionVariationVO todaySessionVariationVO = adminService.getTodaySessionVariation();
        return Result.success(todaySessionVariationVO);
    }

    @GetMapping("/weekSessionVariation")
    @ApiOperation(value = "7日咨询数量变化")
    public Result<WeekSessionVariationVO> getWeekSessionVariation() {
        WeekSessionVariationVO weekSessionVariationVO = adminService.getWeekSessionVariation();
        return Result.success(weekSessionVariationVO);
    }

    @GetMapping("/counselorNumRank")
    @ApiOperation(value = "咨询师咨询数量排名")
    public Result<List<CounselorNumRankVO>> getCounselorNumRank() {
        List<CounselorNumRankVO> counselorNumRankVOList = adminService.getCounselorNumRank();
        return Result.success(counselorNumRankVOList);
    }

    @GetMapping("/counselorRatingRank")
    @ApiOperation(value = "咨询师咨询评分排名")
    public Result<List<CounselorRatingRankVO>> getCounselorRatingRank() {
        List<CounselorRatingRankVO> counselorRatingRankVOList = adminService.getCounselorRatingRank();
        return Result.success(counselorRatingRankVOList);
    }

    @PutMapping("/user/ban/{userId}")
    @ApiOperation(value = "禁用用户")
    public Result banUser(@PathVariable Long userId) {
        adminService.banUser(userId);
        return Result.success();
    }

    @PutMapping("/user/unban/{userId}")
    @ApiOperation(value = "解禁用户")
    public Result unbanUser(@PathVariable Long userId) {
        adminService.unbanUser(userId);
        return Result.success();
    }

}
