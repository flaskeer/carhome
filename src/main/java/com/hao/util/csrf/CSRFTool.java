package com.hao.util.csrf;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by user on 2016/3/31.
 */
public class CSRFTool {

    public String getToken(HttpServletRequest request) {
        return CSRFManager.getToken(request.getSession());
    }

}
