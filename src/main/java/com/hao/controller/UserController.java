package com.hao.controller;

import com.hao.enums.TypeMessageEnum;
import com.hao.model.UserResponse;
import com.hao.model.vo.UserVo;
import com.hao.service.UserService;
import com.hao.util.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.hao.constants.Constants.LOGIN_URI;
import static com.hao.util.Asserts.checkNotBlank;

/**
 * Created by user on 2016/2/24.
 */
@Controller
public class UserController extends AbstractController{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @RequestMapping(value = "/user/login",method = RequestMethod.GET)
    public String login(Model model){
        return "/user/login";
    }

    @RequestMapping(value = "/user/isLogin",method = RequestMethod.POST)
    public String loginPost(UserVo userVo, HttpServletRequest request,Model model){
        try {
            checkNotBlank(userVo.getUserIdentity(), "用户登录名不能为空");
            checkNotBlank(userVo.getUserPassword(), "用户密码不能为空");
            UserResponse response = userService.login(userVo.getUserIdentity(), userVo.getUserPassword());
            if (response.isSuccess()) {
                SessionManager.INSTANCE.login(response.getUserVo(), request.getSession());
                return "redirect:/index";
            }
            setMessage(model, TypeMessageEnum.ERROR, response.getResponse());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("",e);
            setMessage(model,TypeMessageEnum.ERROR,e.getMessage());
        }
        return login(model);
    }

    @RequestMapping(value = "/user/logout")
    public String logout(HttpServletRequest request) {
        SessionManager.INSTANCE.logout(request.getSession());
        return "redirect:" + LOGIN_URI;
    }

    @ExceptionHandler({Exception.class})
    public String exception(Exception e) {
        LOGGER.warn("user controller exception:{}",e.getMessage());
        return "exception";
    }


}
