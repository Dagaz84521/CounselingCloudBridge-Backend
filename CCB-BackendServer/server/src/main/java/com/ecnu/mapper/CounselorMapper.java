package com.ecnu.mapper;

import com.ecnu.entity.Counselor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CounselorMapper {

    /**
     * 根据id获取咨询师信息
     * @param currentId
     * @return
     */
    @Select("select * from counselors where counselor_id = #{currentId}")
    Counselor getById(Long currentId);


    /**
     * 给id对应的咨询师的当前会话数加delta
     * @return
     */
    @Update("UPDATE counselor SET current_sessions = current_sessions + #{delta} WHERE counselor_id = #{counselorId}")
    int updateCurrentSessions(Long counselorId, int delta);
}
