package com.zhangyu.crm.web.filter;

import com.zhangyu.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登陆过的过滤器");
        HttpServletResponse response = null;
        HttpServletRequest request = null;
        if (servletRequest instanceof HttpServletRequest) {
            request = (HttpServletRequest)servletRequest;
        }
        if (servletResponse instanceof HttpServletResponse){
            response = (HttpServletResponse)servletResponse;
        }

        String path = request.getServletPath();
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
}
