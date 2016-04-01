package com.hao.util;

import javax.servlet.http.HttpServletRequest;

/**
 * X-Requested-With:XMLHttpRequest
 * Created by user on 2016/4/1.
 */
public class AjaxUtil {

    private AjaxUtil() {}

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestWith = request.getHeader("X-Requested-With");
        return requestWith!= null && "XMLHttpRequest".equals(requestWith);
    }

}
