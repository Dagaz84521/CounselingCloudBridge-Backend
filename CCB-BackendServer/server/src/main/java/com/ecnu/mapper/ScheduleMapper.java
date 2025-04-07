package com.ecnu.mapper;

import com.ecnu.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScheduleMapper {

    /**
     * 获取咨询师的当月排班信息
     * @param currentId
     * @return
     */
    @Select("select day_of_week from schedule where counselor_id = #{currentId}")
    List<String> getSchedule(Long currentId);

    @Select("select s.day_of_week, s.counselor_id, u.user_type, u.real_name, u.avatar_url from schedule s left outer join users u on s.counselor_id = u.user_id")
    List<ScheduleDTO> getScheduleDTO();

    @Insert("insert into schedule(day_of_week, counselor_id) values(#{dayOfWeek}, #{counselorId})")
    void insertCounselorSchedule(Long counselorId, String dayOfWeek);

    @Delete("delete from schedule where counselor_id = #{counselorId}")
    void deleteCounselorSchedule(Long counselorId);

    @Delete("delete from schedule where counselor_id = #{supervisorId}")
    void deleteSupervisorSchedule(Long supervisorId);

    @Insert("insert into schedule(day_of_week, counselor_id) values(#{day}, #{supervisorId})")
    void insertSupervisorSchedule(Long supervisorId, String day);
}
