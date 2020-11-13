package com.zhangyu.crm.workbench.web.controller;

import com.zhangyu.crm.settings.domain.User;
import com.zhangyu.crm.settings.service.UserService;
import com.zhangyu.crm.settings.service.impl.UserServiceImpl;
import com.zhangyu.crm.utils.*;
import com.zhangyu.crm.vo.PaginationVo;
import com.zhangyu.crm.workbench.domain.Activity;
import com.zhangyu.crm.workbench.service.ActivityService;
import com.zhangyu.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入市场活动控制器");
        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request,response);
        }else if ("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动修改的操作");


        String startDate = request.getParameter("startDate");
        //System.out.println(request.getParameter("owner"));
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String id = request.getParameter("id");
        String endDate = request.getParameter("endDate");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String cost = request.getParameter("cost");

        Activity a = new Activity();
        a.setId(id);
        a.setCost(cost);
        a.setEditBy(editBy);
        a.setEditTime(editTime);
        a.setEndDate(endDate);
        a.setDescription(description);
        a.setName(name);
        a.setOwner(owner);
        a.setStartDate(startDate);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.update(a);
        PrintJson.printJsonFlag(response,flag);


    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Map<String,Object>map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的删除操作");
        String ids[] = request.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());


        boolean flag = as.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入查询市场活动信息列表的操作（结合条件查询和分页查询）");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        System.out.println("----->" + pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算出略过的记录数
        int skipCount = (pageNo - 1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        PaginationVo<Activity> vo = as.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的添加操作");

        String startDate = request.getParameter("startDate");
        //System.out.println(request.getParameter("owner"));
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String id = UUIDUtil.getUUID();
        String endDate = request.getParameter("endDate");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String cost = request.getParameter("cost");

        Activity a = new Activity();
        a.setId(id);
        a.setCost(cost);
        a.setCreateBy(createBy);
        a.setCreateTime(createTime);
        a.setEndDate(endDate);
        a.setDescription(description);
        a.setName(name);
        a.setOwner(owner);
        a.setStartDate(startDate);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.save(a);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response,uList);
    }


}
