package com.ecnu.mapper;

import com.ecnu.annotation.AutoFill;
import com.ecnu.constant.AutoFillConstant;
import com.ecnu.entity.RequestRecord;
import com.ecnu.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RequestRecordMapper {

    @Insert("INSERT INTO request_records VALUES (#{requestId}, #{senderId}, #{receiverId}, #{content})")
    @AutoFill(OperationType.BOTH)
    void insert(RequestRecord requestRecord);
}
