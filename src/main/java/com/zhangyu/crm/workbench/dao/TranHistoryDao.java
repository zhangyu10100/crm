package com.zhangyu.crm.workbench.dao;

import com.zhangyu.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getHistoryListByTranId(String id);
}
