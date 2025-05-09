package com.ecnu.service.impl;

import com.ecnu.constant.SessionStatusConstant;
import com.ecnu.context.BaseContext;
import com.ecnu.dto.ClientCounselorDTO;
import com.ecnu.entity.Counselor;
import com.ecnu.entity.User;
import com.ecnu.mapper.CounselorMapper;
import com.ecnu.mapper.SessionsMapper;
import com.ecnu.mapper.UserMapper;
import com.ecnu.service.ClientService;
import com.ecnu.vo.ClientCounselorDetailVO;
import com.ecnu.vo.ClientHomeVO;
import com.ecnu.vo.ClientSessionVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.concurrent.BackgroundInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private CounselorMapper counselorMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SessionsMapper sessionsMapper;

    /**
     * 获取客户首页信息
     * @return
     */
    public List<ClientHomeVO> getHomeInfo() {
        DayOfWeek dayOfWeek1 = LocalDateTime.now().getDayOfWeek();
        String dayOfWeek = null;
        switch (dayOfWeek1) {
            case MONDAY:
                dayOfWeek = "周一";
                break;
            case TUESDAY:
                dayOfWeek = "周二";
                break;
            case WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case THURSDAY:
                dayOfWeek = "周四";
                break;
            case FRIDAY:
                dayOfWeek = "周五";
                break;
            case SATURDAY:
                dayOfWeek = "周六";
                break;
            case SUNDAY:
                dayOfWeek = "周日";
                break;
        }
        List<Counselor> counselors = counselorMapper.getTop(dayOfWeek);
        List<ClientHomeVO> counselorList = new ArrayList<>();
        for (Counselor counselor : counselors) {
            User user = userMapper.getById(counselor.getCounselorId());
            ClientHomeVO clientHomeVO = new ClientHomeVO();
            clientHomeVO.setCounselorId(counselor.getCounselorId());
            clientHomeVO.setRealName(user.getRealName());
            clientHomeVO.setAvatarUrl(user.getAvatarUrl());
            clientHomeVO.setExpertise(counselor.getExpertise());
            clientHomeVO.setRating(counselor.getRating());
            clientHomeVO.setYearsExperience(counselor.getYearsExperience());
            if(counselor.getCurrentSessions() < 5) {
                clientHomeVO.setIsFree(true);
            } else {
                clientHomeVO.setIsFree(false);
            }
            counselorList.add(clientHomeVO);
        }
        return counselorList;
    }

    /**
     * 获取咨询师排班信息
     * @return
     */
    public List<ClientHomeVO> getCounselorScheduled(ClientCounselorDTO clientCounselorDTO) {
        PageHelper.startPage(clientCounselorDTO.getPage(), clientCounselorDTO.getPagesize());
        DayOfWeek dayOfWeek1 = LocalDateTime.now().getDayOfWeek();
        String dayOfWeek = null;
        switch (dayOfWeek1) {
            case MONDAY:
                dayOfWeek = "周一";
                break;
            case TUESDAY:
                dayOfWeek = "周二";
                break;
            case WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case THURSDAY:
                dayOfWeek = "周四";
                break;
            case FRIDAY:
                dayOfWeek = "周五";
                break;
            case SATURDAY:
                dayOfWeek = "周六";
                break;
            case SUNDAY:
                dayOfWeek = "周日";
                break;
        }
        clientCounselorDTO.setDayOfWeek(dayOfWeek);
        Page<ClientHomeVO> page = counselorMapper.getCounselorScheduled(clientCounselorDTO);
        return page.getResult();
    }

    public ClientSessionVO getSession() {
        ClientSessionVO clientSessionVO = sessionsMapper.getClientSession(BaseContext.getCurrentId());
        return clientSessionVO;
    }

    public ClientCounselorDetailVO getCounselorDetailById(Long counselorId) {
        return new ClientCounselorDetailVO(counselorMapper.getBio(counselorId), sessionsMapper.getTotalSessions(counselorId, SessionStatusConstant.CLOSED));
    }

}
