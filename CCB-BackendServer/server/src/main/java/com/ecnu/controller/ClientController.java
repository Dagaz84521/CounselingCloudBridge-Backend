package com.ecnu.controller;

import com.ecnu.dto.ClientCounselorDTO;
import com.ecnu.result.Result;
import com.ecnu.service.ClientService;
import com.ecnu.vo.ClientHomeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@Slf4j
@Api(tags = "客户相关接口")
public class ClientController {

    @Autowired
    private ClientService clientService;

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


}
