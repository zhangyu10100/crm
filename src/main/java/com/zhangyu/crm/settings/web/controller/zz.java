package com.zhangyu.crm.settings.web.controller;

import com.zhangyu.crm.settings.service.UserService;
import com.zhangyu.crm.settings.service.impl.UserServiceImpl;
import com.zhangyu.crm.utils.ServiceFactory;

public class zz {
    public static void main(String[] args) {
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());


    }

}
