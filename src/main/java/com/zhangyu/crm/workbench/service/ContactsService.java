package com.zhangyu.crm.workbench.service;

import com.zhangyu.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {

    List<Contacts> getContactsListByName(String cname);
}
