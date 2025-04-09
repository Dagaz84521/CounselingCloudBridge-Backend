package com.ecnu.mapper;

import com.ecnu.annotation.AutoFill;
import com.ecnu.entity.SessionRecord;
import com.ecnu.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SessionRecordMapper {
    @Insert({
            "INSERT INTO session_records (session_id, sender_id, receiver_id, content, anonymized_data)",
            "VALUES (#{sessionId}, #{senderId}, #{receiverId}, #{content}, #{anonymizedData})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    @AutoFill(OperationType.CREATE)
    int insert(SessionRecord record);

    @Select({
            "SELECT * FROM session_record",
            "WHERE session_id = #{sessionId}",
            "ORDER BY created_at DESC",
            "LIMIT #{size} OFFSET #{offset}"
    })
    List<SessionRecord> selectBySessionId(@Param("sessionId") Long sessionId, @Param("offset") Long offset, @Param("size") Long size);


    @Update({
            "UPDATE session_record",
            "SET status = #{status}",
            "WHERE record_id = #{recordId}"
    })
    void updateRecordStatus(@Param("recordId") Long recordId, @Param("status") String status);
}
