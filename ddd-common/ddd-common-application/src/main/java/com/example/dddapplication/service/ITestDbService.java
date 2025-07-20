package com.example.dddapplication.service;


import com.example.ddddomain.model.TestDbModel;
import com.example.dddinfrastructure.multiple.DataSource;
import com.example.dddinfrastructure.multiple.DataSourceType;

import java.util.List;
public interface ITestDbService {

    int save();

    List<TestDbModel> findList();

    int saveMaster();
}
