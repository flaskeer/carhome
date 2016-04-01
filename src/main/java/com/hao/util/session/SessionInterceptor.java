package com.hao.util.session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.hao.constants.Constants.LOGIN_URI;
import static com.hao.constants.Constants.LOGIN_USER;

/**
 * Created by user on 2016/3/31.
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Value("${login.exclued.uri}")
    private String[] excludeUris;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isInclude(request.getRequestURI())) {
            if (SessionManager.INSTANCE.isNotLogin(request.getSession())) {
                response.sendRedirect(LOGIN_URI);
                return false;
            }
            request.setAttribute(LOGIN_USER,SessionManager.INSTANCE.getLoginUser(request.getSession()));
        }
        return true;
    }

    private boolean isExclued(String uri) {
        if (excludeUris != null && excludeUris.length > 0 ) {
            for (String excludeUri : excludeUris) {
                if (StringUtils.isNotBlank(excludeUri) && excludeUri.trim().equals(uri)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInclude(String uri) {
        return !isExclued(uri);
    }
}
