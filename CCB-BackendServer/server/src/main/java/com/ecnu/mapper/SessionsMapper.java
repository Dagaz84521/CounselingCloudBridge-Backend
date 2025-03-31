package com.ecnu.mapper;

import com.ecnu.entity.Session;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SessionsMapper {

    @Select("select count(*) from sessions where counselor_id = #{currentId}")
    Integer getTotalSessions(Long currentId);

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
