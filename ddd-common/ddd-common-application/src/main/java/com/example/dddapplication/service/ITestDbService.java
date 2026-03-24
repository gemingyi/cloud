package com.example.dddapplication.service;


import com.example.ddddomain.model.TestDbModel;
import com.example.ddddomain.req.TestDbModelDetailReq;
import com.example.ddddomain.req.TestDbModelQueryReq;
import com.example.pluginmysql.model.page.PageVO;

import java.util.List;
public interface ITestDbService {

    int save();

    List<TestDbModel> findList();

    int saveMaster();


    //---------------- ----------------
    Integer insert(TestDbModel req);

    Integer delete(TestDbModelDetailReq req);

    Integer update(TestDbModel req);

    PageVO<TestDbModel> findPage(TestDbModelQueryReq req);

    TestDbModel findDetail(TestDbModelDetailReq req);

}
