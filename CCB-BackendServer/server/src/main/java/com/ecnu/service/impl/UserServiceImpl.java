package com.ecnu.service.impl;

import com.ecnu.constant.CommonStatusConstant;
import com.ecnu.constant.MessageConstant;
import com.ecnu.constant.UserTypeConstant;
import com.ecnu.dto.UserLoginDTO;
import com.ecnu.dto.UserRegisterDTO;
import com.ecnu.entity.User;
import com.ecnu.exception.AccountBannedException;
import com.ecnu.exception.AccountInactivedException;
import com.ecnu.exception.AccountNotFoundException;
import com.ecnu.exception.PasswordErrorException;
import com.ecnu.mapper.UserMapper;
import com.ecnu.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    public User login(UserLoginDTO userLoginDTO) {
        String phoneNumber = userLoginDTO.getPhoneNumber();
        String password = userLoginDTO.getPassword();

        //根据手机号查询用户
        User user = userMapper.getByPhoneNumber(phoneNumber);
        //用户不存在
        if(user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //对前端传递的明文密码进行加密，然后和数据库中的密文密码进行比对
        String passwordHash = DigestUtils.md5DigestAsHex(password.getBytes());
        //密码错误
        if(!passwordHash.equals(user.getPasswordHash())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        //账号被封禁
        if(user.getStatus().equals(CommonStatusConstant.BANNED)) {
            throw new AccountBannedException(MessageConstant.ACCOUNT_BANNED);
        }
        //账号未激活
        if(user.getStatus().equals(CommonStatusConstant.INACTIVE)) {
            throw new AccountInactivedException(MessageConstant.ACCOUNT_INACTIVED);
        }

        return user;
    }

    /**
     * 用户注册
     * @param userRegisterDTO
     */
    public void register(UserRegisterDTO userRegisterDTO) {
        User user = new User();

        BeanUtils.copyProperties(userRegisterDTO, user);

        user.setUserType(UserTypeConstant.CLIENT);
        user.setPasswordHash(DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes()));
        user.setStatus(CommonStatusConstant.ACTIVE);

        userMapper.register(user);
    }
}
