package com.hao.constants;

import java.util.regex.Pattern;

/**
 * Created by user on 2016/2/24.
 */
public interface Constants {

    //登录页URI
    String LOGIN_URI = "/user/login";

    String X_FORWARD_FOR_HEADER = "x-forward-for";

    String LOGIN_USER = "loginUser";

    String LOGIN_USER_IN_SESSION = "current_login_user";

    Pattern BRACE_SPLIT_PATTERN = Pattern.compile("\\s*(\\{S*\\?})+\\s*");
}
