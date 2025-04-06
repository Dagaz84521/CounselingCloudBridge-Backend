package com.ecnu.service.impl;

import com.ecnu.constant.CommonStatusConstant;
import com.ecnu.constant.MessageConstant;
import com.ecnu.constant.UserTypeConstant;
import com.ecnu.context.BaseContext;
import com.ecnu.dto.ResetPasswordDTO;
import com.ecnu.dto.SmsDTO;
import com.ecnu.dto.UserLoginDTO;
import com.ecnu.dto.UserRegisterDTO;
import com.ecnu.entity.User;
import com.ecnu.exception.*;
import com.ecnu.mapper.UserMapper;
import com.ecnu.service.UserService;
import com.ecnu.utils.SmsUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SmsUtil smsUtil;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    public User login(UserLoginDTO userLoginDTO) {
        String phoneNumber = userLoginDTO.getPhoneNumber();
        String passwordHash = userLoginDTO.getPasswordHash();

        //根据手机号查询用户
        User user = userMapper.getByPhoneNumber(phoneNumber);
        //用户不存在
        if(user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
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
        String phoneNumber = userRegisterDTO.getPhoneNumber();
        if(userMapper.getByPhoneNumber(phoneNumber) == null) {
            throw new AccountHasExistedException(MessageConstant.ACCOUNT_HAS_EXISTED);
        }
        User user = new User();

        BeanUtils.copyProperties(userRegisterDTO, user);

        user.setUserType(UserTypeConstant.CLIENT);
        user.setStatus(CommonStatusConstant.ACTIVE);

        userMapper.register(user);
    }

    /**
     * 重置密码
     * @param resetPasswordDTO
     */
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {

    }

    /**
     * 根据用户id获取用户信息
     * @param currentId
     * @return
     */
    public User getByUserId(Long currentId) {
        User user = userMapper.getById(currentId);
        return user;
    }

    /**
     * 更新用户信息
     * @param user
     */
    public void update(User user) {
        userMapper.update(user);
    }

    /**
     * 获取验证码
     * @param phoneNumber
     */
    public void getCode(String phoneNumber) {
        int code = (int)((Math.random() * 9 + 1) * 100000);
        SmsDTO smsDTO = new SmsDTO(phoneNumber, String.valueOf(code));
        smsUtil.sendSms(smsDTO);
    }

    public void logout() {
        userMapper.logout(BaseContext.getCurrentId());
    }
}
