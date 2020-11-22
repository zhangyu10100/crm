package com.zhangyu.crm.workbench.dao;

import com.zhangyu.crm.workbench.domain.Customer;

public interface CustomerDao {


    int save(Customer cus);

    Customer getCustomerByName(String company);
}
