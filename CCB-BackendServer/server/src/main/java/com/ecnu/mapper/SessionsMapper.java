package com.ecnu.mapper;

import com.ecnu.dto.CounselorTodaySessionVO;
import com.ecnu.entity.Session;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SessionsMapper {

    /**
     * 获取咨询师总咨询次数
     * @param currentId
     * @return
     */
    @Select("select count(*) from sessions where counselor_id = #{currentId} and status = #{status} ")
    Integer getTotalSessions(Long currentId, String status);

    /**
     * 获取咨询师今日咨询次数和时长
     * @param currentId
     * @return
     */
    @Select("select count(*) as total_sessions, sum(timestampdiff(second, start_time, end_time)) as today_hours from sessions where counselor_id = #{currentId} and status = #{status} and date(start_time) = curdate() group by counselor_id ")
    CounselorTodaySessionVO getCounselorTodaySession(Long currentId, String status);

    /**
     * 获取咨询师最近三次咨询
     * @param currentId
     * @return
     */
    @Select("select * from sessions where counselor_id = #{currentId} and status = #{status} order by start_time desc limit 3")
    List<Session> getRecentSessions(Long currentId, String status);

    /**
     * 获取咨询师当前咨询信息
     * @param currentId
     * @return
     */
    @Select("select * from sessions where counselor_id = #{currentId} and status = #{status}")
    List<Session> getSessionList(Long currentId, String status);
}
