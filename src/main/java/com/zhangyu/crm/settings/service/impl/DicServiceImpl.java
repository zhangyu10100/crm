package com.zhangyu.crm.settings.service.impl;

import com.zhangyu.crm.settings.dao.DicTypeDao;
import com.zhangyu.crm.settings.dao.DicValueDao;
import com.zhangyu.crm.settings.domain.DicType;
import com.zhangyu.crm.settings.domain.DicValue;
import com.zhangyu.crm.settings.service.DicService;
import com.zhangyu.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String,List<DicValue>> map = new HashMap<>();

        List<DicType> dtList = dicTypeDao.getTypeList();
        for (DicType dt:dtList){
            String code = dt.getCode();
            List<DicValue> dvList =  dicValueDao.getListByCode(code);
            map.put(code+"List",dvList);
        }

        return map;
    }
}
