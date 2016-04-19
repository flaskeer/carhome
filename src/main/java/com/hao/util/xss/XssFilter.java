package com.hao.util.xss;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by user on 2016/4/19.
 */
public class XssFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XssFilterWrapper((HttpServletRequest) request),response);
    }

    @Override
    public void destroy() {

    }
}
