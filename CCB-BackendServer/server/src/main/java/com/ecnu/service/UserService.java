package com.ecnu.service;

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
}
