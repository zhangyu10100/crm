package com.zhangyu.crm.workbench.service.impl;

import com.zhangyu.crm.utils.SqlSessionUtil;
import com.zhangyu.crm.workbench.dao.CustomerDao;
import com.zhangyu.crm.workbench.domain.Customer;
import com.zhangyu.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<Customer> getCustomerName(String name) {

        List<Customer> cList = customerDao.getCustomerName(name);
        return cList;
    }
}
