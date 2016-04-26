package com.hao.service;

import com.hao.model.UserResponse;
import com.hao.model.vo.UserVo;

/**
 * Created by user on 2016/2/24.
 */

public interface UserService {

    /**
     * 用户登录
     * @param username  登录名
     * @param password   用户密码
     * @return   密码校验结果
     */
    UserResponse login(String username, String password);

    /**
     * 用户注册
     * @param userVo   用户页面实体
     * @return
     */
    UserResponse register(UserVo userVo);

    boolean activeUser(Long userId);

    boolean checkExistUser(String userIdentity,String userEmail);

    UserVo queryUserById(Long userId);
}
