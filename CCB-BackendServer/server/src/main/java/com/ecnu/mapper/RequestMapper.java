package com.ecnu.mapper;

import com.ecnu.dto.SupervisorTodayRequestDTO;
import com.ecnu.entity.SupervisionRequest;
import com.ecnu.vo.RecentRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RequestMapper {

    @Select("select * from supervision_request where supervisor_id = #{currentId} and status = #{status}")
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
}
