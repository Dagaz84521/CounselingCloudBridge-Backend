package com.ecnu.mapper;

import com.ecnu.annotation.AutoFill;
import com.ecnu.entity.User;
import com.ecnu.enumeration.OperationType;
import com.ecnu.vo.UserInfoVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    User geById(Long currentId);

    /**
     * 更新用户信息
     * @param user
     */
    @Update("update users set real_name = #{realName}, age = #{age}, gender = #{gender}, occupation = #{occupation}, avatar_url = #{avatarUrl} where user_id = #{userId}")
    @AutoFill(OperationType.UPDATE)
    void update(User user);
}
