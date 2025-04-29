package com.ecnu.mapper;

import com.ecnu.annotation.AutoFill;
import com.ecnu.dto.AdminCounselorDTO;
import com.ecnu.dto.OnlineCounselorDTO;
import com.ecnu.entity.User;
import com.ecnu.enumeration.OperationType;
import com.ecnu.vo.AdminSupervisorVO;
import com.ecnu.vo.OnlineSupervisor;
import com.ecnu.vo.SupervisorListVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 用户注册
     * @param user
     */
    @Insert("insert into users(phone_number, real_name, password_hash, age, gender, occupation, user_type, status, avatar_url)" +
            "values" +
            "(#{phoneNumber}, #{realName}, #{passwordHash}, #{age}, #{gender}, #{occupation}, #{userType}, #{status}, #{avatarUrl})")
    @AutoFill(OperationType.BOTH)
    void register(User user);

    /**
     * 根据手机号查询用户
     * @param phoneNumber
     * @return
     */
    @Select("select * from users where phone_number = #{phoneNumber}")
    User getByPhoneNumber(String phoneNumber);

    /**
     * 根据id查询用户
     * @param currentId
     * @return
     */
    @Select("select * from users where user_id = #{currentId}")
    User getById(Long currentId);

    /**
     * 更新用户信息
     * @param user
     */
    @AutoFill(OperationType.UPDATE)
    void update(User user);

    @Update("update users set real_name = #{realName} where user_id = #{counselorId}")
    @AutoFill(OperationType.UPDATE)
    void updateCounselor(Long counselorId, String realName);

    Page<AdminSupervisorVO> getSupervisorList(AdminCounselorDTO adminCounselorDTO);

    @Select("select user_id as supervisorId, real_name from users where user_type = 'supervisor'")
    List<SupervisorListVO> supervisorList();

    @Update("update users set status = 'inactive' where user_id = #{currentId}")
    void logout(Long currentId);

    Page<OnlineSupervisor> getOnlineSupervisor(OnlineCounselorDTO onlineCounselorDTO);

    @Update("update users set real_name = #{realName} where user_id = #{supervisorId}")
    @AutoFill(OperationType.UPDATE)
    void updateSupervisor(Long supervisorId, String realName);

    @Update("update users set status = 'banned' where user_id = #{userId}")
    @AutoFill(OperationType.UPDATE)
    void banUser(Long userId);

    @Update("update users set status = 'inactive' where user_id = #{userId}")
    @AutoFill(OperationType.UPDATE)
    void unbanUser(Long userId);
}
