package com.ecnu.mapper;

import com.ecnu.annotation.AutoFill;
import com.ecnu.constant.AutoFillConstant;
import com.ecnu.entity.RequestRecord;
import com.ecnu.entity.SessionRecord;
import com.ecnu.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RequestRecordMapper {

    @Insert("INSERT INTO request_records VALUES (#{requestId}, #{senderId}, #{receiverId}, #{content})")
    @AutoFill(OperationType.CREATE)
    void insert(RequestRecord requestRecord);



    List<RequestRecord> selectByRequestId(@Param("requestId") Long requestId, @Param("offset") Long offset, @Param("size") Long size);
}
