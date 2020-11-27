package com.zhangyu.crm.workbench.service;

import com.zhangyu.crm.workbench.domain.Tran;
import com.zhangyu.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran t, String customerName);

    List<Tran> pageList(Map<String, Object> map);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String id);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();
}
