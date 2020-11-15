package com.zhangyu.crm.workbench.service.impl;

import com.zhangyu.crm.settings.dao.UserDao;
import com.zhangyu.crm.settings.domain.User;
import com.zhangyu.crm.utils.SqlSessionUtil;
import com.zhangyu.crm.vo.PaginationVo;
import com.zhangyu.crm.workbench.dao.ActivityDao;
import com.zhangyu.crm.workbench.dao.ActivityRemarkDao;
import com.zhangyu.crm.workbench.domain.Activity;
import com.zhangyu.crm.workbench.domain.ActivityRemark;
import com.zhangyu.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Override
    public boolean save(Activity a) {
        boolean flag = true;

        int count = activityDao.save(a);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {

        //取得total
        int total = activityDao.getTotalByCondition(map);


        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //创建一个vo对象将total和dataList封装到vo对象
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setDataList(dataList);
        vo.setTotal(total);
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        //删除备注，返回受到影响的条数
        int count2 = activityRemarkDao.deleteByAids(ids);
        //删除市场活动
        if(count1 != count2){
            flag = false;
        }

        int count3 = activityDao.delete(ids);
        if (count3 != ids.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        List<User> uList = userDao.getUserList();

        Activity a = activityDao.getById(id);

        Map<String,Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("a",a);
        return map;
    }

    @Override
    public boolean update(Activity a) {
        boolean flag = true;

        int count = activityDao.update(a);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity a = activityDao.detail(id);
        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);

        return arList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;

        int count = activityRemarkDao.deleteById(id);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(ar);
        if (count != 1){
            flag = false;
        }

        return flag;
    }
}
