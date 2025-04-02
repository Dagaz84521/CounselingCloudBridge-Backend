package com.ecnu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CounselorScheduleMapper {

    /**
     * 获取咨询师的当月排班信息
     * @param currentId
     * @return
     */
    @Select("select date(start_time) as date from counselor_schedule where counselor_id = #{currentId} and month(start_time) = month(curdate()) and year(start_time) = year(curdate()) ")
    List<LocalDate> getSchedule(Long currentId);
}
