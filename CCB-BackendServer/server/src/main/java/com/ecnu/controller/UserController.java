package com.ecnu.controller;

import com.ecnu.context.BaseContext;
import com.ecnu.dto.ResetPasswordDTO;
import com.ecnu.dto.UserRegisterDTO;
import com.ecnu.properties.JwtProperties;
import com.ecnu.constant.JwtClaimsConstant;
import com.ecnu.dto.UserLoginDTO;
import com.ecnu.entity.User;
import com.ecnu.result.Result;
import com.ecnu.service.UserService;
import com.ecnu.utils.JwtUtil;
import com.ecnu.vo.UserInfoVO;
import com.ecnu.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Api(tags = "用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录: {}", userLoginDTO);

        User user = userService.login(userLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .userId(user.getUserId())
                .userType(user.getUserType())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册: {}", userRegisterDTO);

        userService.register(userRegisterDTO);

        return Result.success();
    }

    /**
     * 重置密码
     * @param resetPasswordDTO
     * @return
     */
    @PutMapping("/password")
    @ApiOperation(value = "重置密码")
    public Result resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        log.info("重置密码:{}", resetPasswordDTO);

        userService.resetPassword(resetPasswordDTO);

        return Result.success();
    }

    /**
     * 获取验证码
     * @param phoneNumber
     * @return
     */
    @GetMapping("/getcode")
    @ApiOperation(value = "获取验证码")
    public Result getCode(@RequestParam String phoneNumber) {
        log.info("获取验证码:{}", phoneNumber);

        userService.getCode(phoneNumber);

        return Result.success();
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息")
    public Result<UserInfoVO> getInfo() {
        log.info("获取用户信息");

        User user = userService.getByUserId(BaseContext.getCurrentId());
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);

        return Result.success(userInfoVO);
    }

    @PutMapping("/info")
    @ApiOperation(value = "修改用户信息")
    public Result updateInfo(@RequestBody UserInfoVO userInfoVO) {
        log.info("修改用户信息:{}", userInfoVO);

        User user = new User();
        BeanUtils.copyProperties(userInfoVO, user);
        user.setUserId(BaseContext.getCurrentId());
        userService.update(user);

        return Result.success();
    }

}
