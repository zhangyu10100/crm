package com.zhangyu.crm.workbench.dao;

import com.zhangyu.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {



    int save(Tran t);

    List<Tran> pageList(Map<String, Object> map);

    Tran detail(String id);

    int changeStage(Tran t);

    int getTotal();

    List<Map<String, Object>> getCharts();
}
