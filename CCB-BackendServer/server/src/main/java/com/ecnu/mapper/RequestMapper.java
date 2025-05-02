package com.ecnu.mapper;

import com.ecnu.dto.CounselorHistoryDTO;
import com.ecnu.dto.SupervisorTodayRequestDTO;
import com.ecnu.entity.SupervisionRequest;
import com.ecnu.vo.RecentRequest;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RequestMapper {

    List<SupervisionRequest> getRequestList(Long currentId, String status);

    @Select("select count(*) from supervision_request where supervisor_id = #{currentId} and status = #{status}")
    Long getTotalRequests(Long currentId, String status);

    @Select("select count(*) as todayRequests, sum(timestampdiff(second, start_time, end_time)) as todayHours from supervision_request where supervisor_id = #{currentId} and status = #{status} and date(start_time) = curdate() group by supervisor_id")
    SupervisorTodayRequestDTO getSupervisorTodayRequest(Long currentId, String status);

    @Select("select r.request_id, r.counselor_id, r.start_time, u.real_name, timestampdiff(second, r.start_time, r.end_time) as duration from supervision_request r left outer join users u on r.counselor_id = u.user_id where r.supervisor_id = #{currentId} and r.status = #{status} order by r.start_time desc limit 3")
    List<RecentRequest> getRecentRequests(Long currentId, String status);

    @Select("select count(*) from supervision_request where status = #{status}")
    Long getCurrentRequests(String status);

    @Select("select sum(timestampdiff(second, start_time, end_time)) from supervision_request where supervisor_id = #{supervisorId} and status = #{status}")
    Long getTotalHours(Long supervisorId, String status);

    @Select("select * from supervision_request where supervisor_id = #{supervisorId} and counselor_id = #{counselorId} and status = 'pending'")
    SupervisionRequest getPendingByParticipationId(Long supervisorId, Long counselorId);

    /**
     * @param supervisionRequest 包含要更新的数据的 SupervisionRequest 对象，其 requestId 必须有效。
     * @return 受影响的行数
     */
    @Update({
            "<script>",
            "UPDATE supervision_request",
            "<set>",
            "  <if test='supervisorId != null'>supervisor_id = #{supervisorId},</if>",
            "  <if test='counselorId != null'>counselor_id = #{counselorId},</if>",
            "  <if test='relationId != null'>relation_id = #{relationId},</if>",
            "  <if test='startTime != null'>start_time = #{startTime},</if>",
            "  <if test='endTime != null'>end_time = #{endTime},</if>",
            "  <if test='requestDetails != null'>request_details = #{requestDetails},</if>",
            "  <if test='status != null'>status = #{status},</if>",
            "  <if test='sessionId != null'>session_id = #{sessionId},</if>",
            "</set>",
            "WHERE request_id = #{requestId}",
            "</script>"
    })
    void update(SupervisionRequest supervisionRequest);

    /**
     * @param requestId 要更新的记录的ID号
     * @return ID对应的记录
     */
    @Select("select * from supervision_request where request_id = #{requestId}")
    SupervisionRequest getById(Long requestId);

    /**
     * @param request 要插入的 SupervisionRequest 对象。
     * @return 受影响的行数，通常为 1。
     */
    @Insert({
            "INSERT INTO supervision_request (",
            "  supervisor_id, counselor_id, relation_id, session_id, start_time, request_details, status",
            ") VALUES (",
            "  #{supervisorId}, #{counselorId}, #{relationId}, #{sessionId}, #{startTime},  #{requestDetails}, #{status}",
            ")"
    })
    @Options(useGeneratedKeys = true, keyProperty = "requestId", keyColumn = "request_id")
    int insert(SupervisionRequest request);

    Page<RecentRequest> getHistory(CounselorHistoryDTO counselorHistoryDTO, Long currentId);


    @Select("select * from supervision_request where session_id = #{sessionId} and status = 'accepted'")
    SupervisionRequest getBySessionId(Long sessionId);
}
