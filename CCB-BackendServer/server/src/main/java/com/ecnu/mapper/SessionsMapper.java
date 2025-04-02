package com.ecnu.mapper;


import com.ecnu.dto.CounselorTodaySessionVO;
import com.ecnu.entity.Session;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ecnu.entity.Session;
import org.apache.ibatis.annotations.*;

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


}
