package com.hao.util.session;

import com.hao.model.vo.UserVo;

import javax.servlet.http.HttpSession;

import static com.hao.constants.Constants.LOGIN_USER_IN_SESSION;

/**
 * Created by user on 2016/2/24.
 */
public enum SessionManager {

    INSTANCE;

    public boolean isLogin(HttpSession session){
        UserVo userVo = getLoginUser(session);
        return userVo != null;
    }

    public boolean isNotLogin(HttpSession session){
        return !isLogin(session);
    }

    public void login(UserVo userVo,HttpSession session){
        session.setAttribute(LOGIN_USER_IN_SESSION,userVo);
    }

    public void logout(HttpSession session){
        session.invalidate();
    }

    public UserVo getLoginUser(HttpSession session){
        return (UserVo) session.getAttribute(LOGIN_USER_IN_SESSION);
    }
}
