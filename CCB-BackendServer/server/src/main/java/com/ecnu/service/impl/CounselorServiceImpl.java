package com.ecnu.service.impl;

import com.ecnu.context.BaseContext;
import com.ecnu.entity.Counselor;
import com.ecnu.entity.User;
import com.ecnu.mapper.CounselorMapper;
import com.ecnu.mapper.SessionsMapper;
import com.ecnu.mapper.UserMapper;
import com.ecnu.service.CounselorService;
import com.ecnu.vo.CounselorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounselorServiceImpl implements CounselorService {

    @Autowired
    private CounselorMapper counselorMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SessionsMapper sessionsMapper;

    /**
     * 获取咨询师信息
     * @return
     */
    public CounselorInfo getCounselorInfo() {
        User user = userMapper.geById(BaseContext.getCurrentId());
        Counselor counselor = counselorMapper.getById(BaseContext.getCurrentId());
        CounselorInfo counselorInfo = new CounselorInfo().builder()
                .realName(user.getRealName())
                .avatarUrl(user.getAvatarUrl())
                .totalSessions(sessionsMapper.getTotalSessions(BaseContext.getCurrentId()))
                .currentSessions(counselor.getCurrentSessions())
                .build();
        return null;
    }
}
