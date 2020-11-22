package com.zhangyu.crm.workbench.service.impl;

import com.zhangyu.crm.utils.SqlSessionUtil;
import com.zhangyu.crm.workbench.dao.ContactsDao;
import com.zhangyu.crm.workbench.domain.Contacts;
import com.zhangyu.crm.workbench.service.ContactsService;

import java.util.List;

public class ContactsServiceImpl implements ContactsService {
    ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);

    @Override
    public List<Contacts> getContactsListByName(String cname) {

        List<Contacts> cList = contactsDao.getContactsListByName(cname);

        return cList;
    }
}
