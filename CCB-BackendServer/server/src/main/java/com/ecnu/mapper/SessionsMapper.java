package com.ecnu.mapper;


import com.ecnu.dto.*;
import com.ecnu.entity.Session;
import com.ecnu.vo.ClientSessionVO;
import com.ecnu.vo.CounselorNumRankVO;
import com.ecnu.vo.CounselorRatingRankVO;
import com.ecnu.vo.RecentSession;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
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


    @Select("SELECT * FROM sessions WHERE session_id = #{sessionId}")
    Session getById(@Param("sessionId") Long sessionId);

    @Update("UPDATE sessions SET status = #{status}, end_time = #{endTime}, rating = #{rating} WHERE session_id = #{sessionId}")
    int updateSessionStatus(Session session);

    @Select("SELECT * FROM sessions WHERE client_id = #{userId} OR counselor_id = #{userId} ORDER BY start_time DESC")
    List<Session> selectSessionsByUser(@Param("userId") Long userId);

    @Insert("INSERT INTO sessions (client_id, counselor_id, status, start_time) " +
            "VALUES (#{clientId}, #{counselorId}, #{status}, #{startTime})")
    @Options(useGeneratedKeys = true, keyProperty = "sessionId")
    int insertSession(Session session);

    /**
     * 获取咨询记录
     * @param counselorHistoryDTO
     * @return
     */
    Page<RecentSession> getHistory(CounselorHistoryDTO counselorHistoryDTO, Long currentId);

    /**
     * 添加咨询评价
     * @param sessionAddAdviceDTO
     */
    @Update("UPDATE sessions SET advice = #{advice}, type = #{type} WHERE session_id = #{sessionid}")
    void addSessionAdvice(SessionAddAdviceDTO sessionAddAdviceDTO);

    @Select("select count(*) as todaySessions, sum(timestampdiff(second, start_time, end_time)) as todayHours from sessions where status = #{status} and date(start_time) = curdate()")
    AdminTodaySessionDTO getTodaySession(String status);

    @Select("select count(*) from sessions where status = #{status}")
    Long getCurrentSessions(String status);

    @Select("select sum(timestampdiff(second, start_time, end_time)) from sessions where counselor_id = #{counselorId} and status = #{status}")
    Long getTotalHours(Long counselorId, String status);


    @Select("select s.counselor_id, u.real_name, u.avatar_url, s.session_id from sessions s join users u on s.counselor_id = u.user_id where s.client_id = #{clientId} and s.status = 'active'")
    ClientSessionVO getClientSession(Long clientId);

    @Select("SELECT * FROM sessions WHERE client_id = #{clientId} AND counselor_id = #{counselorId} AND status = 'active'")
    Session getByParticipantIds(Long clientId, Long counselorId);

    @Select("SELECT * FROM sessions WHERE client_id = #{userId} OR counselor_id = #{userId}")
    List<Session>  getByParticipantId(Long userId);

    @Select("SELECT * FROM sessions WHERE client_id = #{clientId}")
    List<Session>  getByClientId(Long clientId);

    /**
     * 获取指定咨询师最早的待处理会话
     * @param counselorId 咨询师ID
     * @return 按时间排序最早的待处理会话，无结果时返回null
     */
    @Select({
            "SELECT * FROM sessions",
            "WHERE counselor_id = #{counselorId}",
            "AND status = 'pending'",
            "ORDER BY start_time ASC",
            "LIMIT 1"
    })
    Session getEarliestPendingSession(Long counselorId);

    @Select("select COUNT(*) from sessions where status = 'closed' and start_time >= #{begin} and start_time < #{end}")
    Long getTodaySessionVariation(LocalDateTime begin, LocalDateTime end);

    @Select("select COUNT(*) from sessions where status = 'closed' and date(start_time) = date(#{date}) ")
    Long getWeekSSessionVariation(LocalDate date);

    @Select("select real_name, avatar_url, tmp.num from (select counselor_id, count(*) as num from sessions where status = 'closed' and date(start_time) = curdate() group by counselor_id order by num desc limit 3) as tmp inner join users on tmp.counselor_id = user_id")
    List<CounselorNumRankVO> getCounselorNumRank();

    @Select("select real_name, avatar_url, tmp.rating from (select counselor_id, avg(rating) as rating from sessions where status = 'closed' and date(start_time) = curdate() group by counselor_id order by rating desc limit 3) as tmp inner join users on tmp.counselor_id= user_id")
    List<CounselorRatingRankVO> getCounselorRatingRank();
}
