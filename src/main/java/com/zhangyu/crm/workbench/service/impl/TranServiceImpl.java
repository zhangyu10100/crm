package com.zhangyu.crm.workbench.service.impl;

import com.zhangyu.crm.utils.DateTimeUtil;
import com.zhangyu.crm.utils.SqlSessionUtil;
import com.zhangyu.crm.utils.UUIDUtil;
import com.zhangyu.crm.workbench.dao.CustomerDao;
import com.zhangyu.crm.workbench.dao.TranDao;
import com.zhangyu.crm.workbench.dao.TranHistoryDao;
import com.zhangyu.crm.workbench.domain.Customer;
import com.zhangyu.crm.workbench.domain.Tran;
import com.zhangyu.crm.workbench.domain.TranHistory;
import com.zhangyu.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran t, String customerName) {

        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setCreateBy(t.getCreateBy());
            customer.setNextContactTime(t.getNextContactTime());
            customer.setOwner(t.getOwner());
            customer.setName(t.getName());
            customer.setContactSummary(t.getContactSummary());

            int count1 = customerDao.save(customer);
            if (count1 != 1){
                flag = false;
            }
        }

        t.setCustomerId(customer.getId());

        int count2 = tranDao.save(t);
        if (count2 != 1){
            flag = false;
        }

        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(t.getId());
        tranHistory.setStage(t.getStage());
        tranHistory.setMoney(t.getMoney());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setExpectedDate(t.getExpectedDate());
        tranHistory.setCreateBy(t.getCreateBy());

        int count3 = tranHistoryDao.save(tranHistory);
        if (count3!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Tran> pageList(Map<String, Object> map) {
        List<Tran> tList = tranDao.pageList(map);
        return tList;
    }

    @Override
    public Tran detail(String id) {
        Tran t = tranDao.detail(id);
        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String id) {
        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(id);
        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;
        int count1 = tranDao.changeStage(t);
        if (count1 != 1){
            flag = false;
        }

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());


        int count2 = tranHistoryDao.save(th);
        if (count2 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        int total = tranDao.getTotal();

        List<Map<String,Object>> dataList = tranDao.getCharts();

        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }
}
