package com.zhangyu.crm.workbench.service.impl;

import com.zhangyu.crm.utils.SqlSessionUtil;
import com.zhangyu.crm.workbench.dao.ClueDao;
import com.zhangyu.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
}
