package com.zhangyu.crm.workbench.web.controller;

import com.zhangyu.crm.settings.domain.User;
import com.zhangyu.crm.settings.service.UserService;
import com.zhangyu.crm.settings.service.impl.UserServiceImpl;
import com.zhangyu.crm.utils.DateTimeUtil;
import com.zhangyu.crm.utils.PrintJson;
import com.zhangyu.crm.utils.ServiceFactory;
import com.zhangyu.crm.utils.UUIDUtil;
import com.zhangyu.crm.vo.PaginationVo;
import com.zhangyu.crm.workbench.domain.*;
import com.zhangyu.crm.workbench.service.*;
import com.zhangyu.crm.workbench.service.impl.*;

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
        }else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        }else if ("/workbench/transaction/save.do".equals(path)) {
            save(request, response);
        }else if ("/workbench/transaction/pageList.do".equals(path)) {
            pageList(request, response);
        }else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request, response);
        }else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)) {
            getHistoryListByTranId(request, response);
        }else if ("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request, response);
        }else if ("/workbench/transaction/getCharts.do".equals(path)) {
            getCharts(request, response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得交易阶段数量统计表的数据");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Map<String,Object> map = ts.getCharts();
        PrintJson.printJsonObj(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行改变阶段的操作");
        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();


        Tran t = new Tran();
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        t.setId(id);
        t.setStage(stage);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.changeStage(t);
        Map<String,String> pMap = (Map<String,String>)request.getServletContext().getAttribute("pMap");
        t.setpossibility(pMap.get(stage));
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("t",t);

        PrintJson.printJsonObj(response,map);

    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据交易id取得相应的历史列表");
        String id = request.getParameter("tranId");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> thList = ts.getHistoryListByTranId(id);
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        for (TranHistory th:thList){
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);
        }

        PrintJson.printJsonObj(response,thList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页");
        String id = request.getParameter("id");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);
        String stage = t.getStage();
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setpossibility(possibility);
        request.setAttribute("t",t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入分页操作");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String stage = request.getParameter("stage");
        String customerName = request.getParameter("customerName");
        String type = request.getParameter("type");
        String contactsName = request.getParameter("contactsName");
        String source = request.getParameter("source");
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
        map.put("stage",stage);
        map.put("source",source);
        map.put("customerName",customerName);
        map.put("type",type);
        map.put("contactsName",contactsName);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);



        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<Tran> tList = ts.pageList(map);
        PrintJson.printJsonObj(response,tList);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("执行添加交易的操作");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setContactSummary(contactSummary);
        t.setContactsId(contactsId);
        t.setDescription(description);
        t.setSource(source);
        t.setStage(stage);
        t.setOwner(owner);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setNextContactTime(nextContactTime);
        t.setType(type);
        t.setActivityId(activityId);
        t.setExpectedDate(expectedDate);
        t.setName(name);
        t.setMoney(money);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t,customerName);
        if (flag){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得客户名称列表，根据客户名称模糊查询");
        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<Customer> cList = cs.getCustomerName(name);
        PrintJson.printJsonObj(response,cList);
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
