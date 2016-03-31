package com.hao.util.csrf;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by user on 2016/3/31.
 */
public class CSRFInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof DefaultServletHttpRequestHandler) {
            return true;
        }
        if (request.getMethod().equals("GET")) {
            return true;
        } else {
            String sessionToken = CSRFManager.getToken(request.getSession());
            String requestToken = CSRFManager.getToken(request);
            if (sessionToken.equals(requestToken)) {
                return true;
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,"Bad or missing CSRF value");
                return false;
            }
        }
    }
}
