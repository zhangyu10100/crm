package com.zhangyu.crm.workbench.service;

import com.zhangyu.crm.vo.PaginationVo;
import com.zhangyu.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {
    boolean save(Activity a);

    PaginationVo<Activity> pageList(Map<String, Object> map);


    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);
}
