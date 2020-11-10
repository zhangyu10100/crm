package com.zhangyu.crm.settings.web.controller;

import com.zhangyu.crm.settings.domain.User;
import com.zhangyu.crm.settings.service.UserService;
import com.zhangyu.crm.settings.service.impl.UserServiceImpl;
import com.zhangyu.crm.utils.MD5Util;
import com.zhangyu.crm.utils.PrintJson;
import com.zhangyu.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入用户控制器");
        String path = request.getServletPath();
        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if ("/settings/user/xxx.do".equals(path)){
            //xxx(request,response);
        }
    }

    private void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("进入登录验证操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");

        loginPwd = MD5Util.getMD5(loginPwd);

        //接受IP地址
        String ip = request.getRemoteAddr();
        System.out.println("ip-------"+ ip);

        //System.out.println("user");
        //未来业务层开发统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        //UserService us = new UserServiceImpl();
        System.out.println("us");
        try{
            User user = us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            //System.out.println("true");
        /*
        * {"success":true}
        * */
            PrintJson.printJsonFlag(response,true);

        }catch (Exception e){
            e.printStackTrace();
            //System.out.println("false");
            //一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛出异常
            //表示登陆失败
            /*
            * {"success":false,"msg":?}
            * */
            String msg = e.getMessage();
            /*
            * 作为controller，需要为Ajax请求提供多项信息
            *   1、将多项信息打包成map，将map解析为json串
            *   2、创建一个vo
            *       包含private boolean success;
            *           private String msg;属性
            * */
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);

        }

    }
}
