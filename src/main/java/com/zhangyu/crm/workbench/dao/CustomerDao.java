package com.zhangyu.crm.workbench.dao;

import com.zhangyu.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {


    int save(Customer cus);

    Customer getCustomerByName(String company);

    List<Customer> getCustomerName(String name);
}
