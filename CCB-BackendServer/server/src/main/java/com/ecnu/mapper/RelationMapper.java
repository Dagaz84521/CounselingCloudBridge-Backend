package com.ecnu.mapper;

import com.ecnu.annotation.AutoFill;
import com.ecnu.entity.CounselorSupervisorRelation;
import com.ecnu.enumeration.OperationType;
import com.ecnu.vo.OnlineCounselor;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


import java.util.List;

@Mapper
public interface RelationMapper {

    Page<OnlineCounselor> getOnlineCounselor(Long currentId);

    @Update("update counselor_supervisor_relation set supervisor_id = #{supervisorId} where counselor_id = #{counselorId}")
    @AutoFill(OperationType.UPDATE)
    void updateByCounselorId(Long counselorId, Long supervisorId);

    @Insert("insert into counselor_supervisor_relation(counselor_id, supervisor_id)" +
            "values(#{counselorId}, #{supervisorId})")
    @AutoFill(OperationType.BOTH)
    void insert(CounselorSupervisorRelation build);
}
