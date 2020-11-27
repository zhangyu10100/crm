package com.zhangyu.crm.workbench.service;

import com.zhangyu.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getCustomerName(String name);
}
