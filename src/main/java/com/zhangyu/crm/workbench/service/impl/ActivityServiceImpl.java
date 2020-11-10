package com.zhangyu.crm.workbench.service.impl;

import com.zhangyu.crm.utils.SqlSessionUtil;
import com.zhangyu.crm.workbench.dao.ActivityDao;
import com.zhangyu.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
}
