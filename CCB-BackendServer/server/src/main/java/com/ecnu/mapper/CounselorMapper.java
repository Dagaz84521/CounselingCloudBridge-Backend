package com.ecnu.mapper;

import com.ecnu.entity.Counselor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CounselorMapper {

    /**
     * 根据id获取咨询师信息
     * @param currentId
     * @return
     */
    @Select("select * from counselors where counselor_id = #{currentId}")
    Counselor getById(Long currentId);
}
