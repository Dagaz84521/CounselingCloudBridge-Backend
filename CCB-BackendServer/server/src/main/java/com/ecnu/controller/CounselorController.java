package com.ecnu.controller;

import com.ecnu.result.Result;
import com.ecnu.service.CounselorService;
import com.ecnu.vo.CounselorHomeVO;
import com.ecnu.vo.CounselorInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        CounselorHomeVO counselorHomeVO = new CounselorHomeVO();
        CounselorInfo counselorInfo = CounselorService.getCounselorInfo();
        return Result.success(counselorHomeVO);
    }

}
