package com.ecnu.service;

import com.ecnu.dto.ResetPasswordDTO;
import com.ecnu.dto.UserLoginDTO;
import com.ecnu.dto.UserRegisterDTO;
import com.ecnu.entity.User;

public interface UserService {

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * 用户注册
     * @param userRegisterDTO
     */
    void register(UserRegisterDTO userRegisterDTO);

    /**
     * 重置密码
     * @param resetPasswordDTO
     */
    void resetPassword(ResetPasswordDTO resetPasswordDTO);

    /**
     * 根据用户id获取用户信息
     * @param currentId
     * @return
     */
    User getByUserId(Long currentId);

    /**
     * 更新用户信息
     * @param user
     */
    void update(User user);

    /**
     * 获取验证码
     * @param phoneNumber
     */
    void getCode(String phoneNumber);

    void logout();

    void resetPhoneNumber(String phoneNumber, String code);
}
