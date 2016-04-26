package com.hao.service.impl;

import com.hao.enums.UserStatusEnum;
import com.hao.mapper.UserRepo;
import com.hao.model.User;
import com.hao.model.UserResponse;
import com.hao.model.vo.UserVo;
import com.hao.service.UserService;
import com.hao.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
/**
 * Created by user on 2016/2/24.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepo userRepo;

    @Override
    public UserResponse login(String username, String password) {
        List<User> userList = queryUserByIdentityOrEmail(username, username);
        if(isNotEmpty(userList)){
            for (User user : userList) {
                if(PasswordUtil.isPasswordVaild(password,user.getUserPassword(),user.getSalt())){
                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(user,userVo);
                    userVo.setUserPassword(null);
                    return new UserResponse(userVo);
                }
            }
        }
        return new UserResponse("用户密码错误或者是用户不存在");
    }

    @Override
    public UserResponse register(UserVo userVo) {

        return null;
    }

    @Override
    public boolean activeUser(Long userId) {
        if (userId != null) {
            User user = userRepo.queryOne(userId);
        }
        return false;
    }

    @Override
    public boolean checkExistUser(String userIdentity, String userEmail) {
        return false;
    }

    @Override
    public UserVo queryUserById(Long userId) {
        return null;
    }

    private List<User> queryUserByIdentityOrEmail(String userIdentity,String userEmail){
        User user = new User();
        user.setUserIdentity(userIdentity);
        user.setUserEmail(userEmail);
        return userRepo.queryExistUser(user);
    }


}
