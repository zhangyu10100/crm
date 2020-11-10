package com.zhangyu.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入过滤字符编码的过滤器");
        //过滤post请求的中文参数乱码
        servletRequest.setCharacterEncoding("utf-8");

        //过滤响应流中文乱码问题
        servletResponse.setContentType("text/html;charset=utf-8");

        //将请求放行
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
