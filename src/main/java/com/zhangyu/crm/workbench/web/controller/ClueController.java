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
import com.zhangyu.crm.workbench.domain.ActivityRemark;
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

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入线索控制器");
        String path = request.getServletPath();
        if ("/workbench/clue/xxx.do".equals(path)) {
            //xxx(request,response);
        } else if ("/workbench/clue/xxx.do".equals(path)) {
            //xxx(request,response);
        }


    }
}
