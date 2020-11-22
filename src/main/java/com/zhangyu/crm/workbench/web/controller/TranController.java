package com.zhangyu.crm.workbench.web.controller;

import com.zhangyu.crm.settings.domain.User;
import com.zhangyu.crm.settings.service.UserService;
import com.zhangyu.crm.settings.service.impl.UserServiceImpl;
import com.zhangyu.crm.utils.DateTimeUtil;
import com.zhangyu.crm.utils.PrintJson;
import com.zhangyu.crm.utils.ServiceFactory;
import com.zhangyu.crm.utils.UUIDUtil;
import com.zhangyu.crm.vo.PaginationVo;
import com.zhangyu.crm.workbench.domain.Activity;
import com.zhangyu.crm.workbench.domain.Clue;
import com.zhangyu.crm.workbench.domain.Contacts;
import com.zhangyu.crm.workbench.domain.Tran;
import com.zhangyu.crm.workbench.service.ActivityService;
import com.zhangyu.crm.workbench.service.ClueService;
import com.zhangyu.crm.workbench.service.ContactsService;
import com.zhangyu.crm.workbench.service.impl.ActivityServiceImpl;
import com.zhangyu.crm.workbench.service.impl.ClueServiceImpl;
import com.zhangyu.crm.workbench.service.impl.ContactsServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入交易控制器");
        String path = request.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)) {
            add(request, response);
        } else if ("/workbench/transaction/getActivityListByName.do".equals(path)) {
            getActivityListByName(request, response);
        } else if ("/workbench/transaction/getContactsListByName.do".equals(path)) {
            getContactsListByName(request, response);
        }
    }

    private void getContactsListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行按照名字模糊查询联系人");

        String cname = request.getParameter("cname");
        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<Contacts> cList = cs.getContactsListByName(cname);
        PrintJson.printJsonObj(response,cList);
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行按照名字模糊查询市场活动");

        String aname = request.getParameter("aname");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByName(aname);
        PrintJson.printJsonObj(response,aList);

    }

    private void add(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转交易添加页的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> ulList = us.getUserList();

        request.setAttribute("uList",ulList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);

    }
}
