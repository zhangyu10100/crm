package com.zhangyu.crm.workbench.service;

import com.zhangyu.crm.vo.PaginationVo;
import com.zhangyu.crm.workbench.domain.Clue;
import com.zhangyu.crm.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {

    boolean save(Clue c);

    PaginationVo<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);


    boolean convert(String clueId, Tran t, String createBy);
}
