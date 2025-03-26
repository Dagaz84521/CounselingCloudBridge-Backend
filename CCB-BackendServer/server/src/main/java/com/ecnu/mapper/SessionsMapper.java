package com.ecnu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SessionsMapper {

    @Select("select count(*) from sessions where counselor_id = #{currentId}")
    Integer getTotalSessions(Long currentId);
}
