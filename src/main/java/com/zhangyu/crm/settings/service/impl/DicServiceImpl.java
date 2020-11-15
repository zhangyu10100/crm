package com.zhangyu.crm.settings.service.impl;

import com.zhangyu.crm.settings.dao.DicTypeDao;
import com.zhangyu.crm.settings.dao.DicValueDao;
import com.zhangyu.crm.settings.service.DicService;
import com.zhangyu.crm.utils.SqlSessionUtil;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

}
