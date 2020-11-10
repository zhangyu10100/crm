package com.zhangyu.crm.settings.service.impl;

import com.zhangyu.crm.exception.LoginException;
import com.zhangyu.crm.settings.dao.UserDao;
import com.zhangyu.crm.settings.domain.User;
import com.zhangyu.crm.settings.service.UserService;
import com.zhangyu.crm.utils.DateTimeUtil;
import com.zhangyu.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    public User login(String loginAct, String loginPwd,String ip) throws LoginException{

        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);

        if (user==null){
            throw new LoginException("账号密码错误");
        }

        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime) < 0){
            /*System.out.println(currentTime);
            System.out.println(expireTime);*/
            throw new LoginException("账号已失效");
        }

        //判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }

        //判断IP地址
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("ip地址受限");
        }
        return user;


    }

    @Override
    public List<User> getUserList() {

        List<User> uList = userDao.getUserList();
        //System.out.println(uList.size());
        return uList;
    }
}
