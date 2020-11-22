package com.zhangyu.crm.settings.dao;

import com.zhangyu.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
