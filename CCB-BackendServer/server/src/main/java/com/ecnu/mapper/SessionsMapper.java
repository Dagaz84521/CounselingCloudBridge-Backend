package com.ecnu.mapper;


import com.ecnu.dto.*;
import com.ecnu.entity.Session;
import com.ecnu.vo.RecentSession;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface SessionsMapper {


    /**
     * 获取咨询师总咨询次数
     * @param currentId
     * @return
     */
    @Select("select count(*) from sessions where counselor_id = #{currentId} and status = #{status} ")
    Long getTotalSessions(Long currentId, String status);

    /**
     * 获取咨询师今日咨询次数和时长
     * @param currentId
     * @return
     */
    @Select("select count(*) as todaySessions, sum(timestampdiff(second, start_time, end_time)) as todayHours from sessions where counselor_id = #{currentId} and status = #{status} and date(start_time) = curdate() group by counselor_id ")
    CounselorTodaySessionDTO getCounselorTodaySession(Long currentId, String status);

    /**
     * 获取咨询师最近三次咨询
     * @param currentId
     * @return
     */
    @Select("select s.session_id, s.client_id, s.start_time, s.rating, u.real_name, timestampdiff(second, s.start_time, s.end_time) as duration from sessions s left outer join users u on s.client_id = u.user_id where s.counselor_id = #{currentId} and s.status = #{status} order by s.start_time desc limit 3")
    List<RecentSession> getRecentSessions(Long currentId, String status);

    /**
     * 获取咨询师当前咨询信息
     * @param currentId
     * @return
     */
    @Select("select * from sessions where counselor_id = #{currentId} and status = #{status}")
    List<Session> getSessionList(Long currentId, String status);


    @Select("SELECT * FROM session WHERE session_id = #{sessionId}")
    Session getById(@Param("sessionId") Long sessionId);

    @Update("UPDATE session SET status = #{status}, end_time = #{endTime} WHERE session_id = #{sessionId}")
    int updateSessionStatus(Session session);

    @Select("SELECT * FROM session WHERE client_id = #{userId} OR counselor_id = #{userId} ORDER BY start_time DESC")
    List<Session> selectSessionsByUser(@Param("userId") Long userId);

    @Insert("INSERT INTO session (client_id, counselor_id, status, start_time) " +
            "VALUES (#{clientId}, #{counselorId}, #{status}, #{startTime})")
    @Options(useGeneratedKeys = true, keyProperty = "sessionId")
    int insertSession(Session session);

    /**
     * 获取咨询记录
     * @param counselorHistoryDTO
     * @return
     */
    Page<RecentSession> getHistory(CounselorHistoryDTO counselorHistoryDTO);

    /**
     * 添加咨询评价
     * @param sessionAddAdviceDTO
     * @param sessionid
     */
    @Update("UPDATE session SET advice = #{advice}, type = #{type} WHERE session_id = #{sessionid}")
    void addSessionAdvice(SessionAddAdviceDTO sessionAddAdviceDTO, Long sessionid);

    @Select("select count(*) as todaySessions, sum(timestampdiff(second, start_time, end_time)) as todayHours from sessions where status = #{status} and date(start_time) = curdate()")
    AdminTodaySessionDTO getTodaySession(String status);

    @Select("select count(*) from sessions where status = #{status}")
    Long getCurrentSessions(String status);

    @Select("select sum(timestampdiff(second, start_time, end_time)) from sessions where counselor_id = #{counselorId} and status = #{status}")
    Long getTotalHours(Long counselorId, String status);
}
