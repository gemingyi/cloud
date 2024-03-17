package com.example.dddapplication.service;


import com.example.ddddomain.model.TestDbModel;

import java.util.List;

public interface ITestDbService {

    int save();

    List<TestDbModel> findList();

}
