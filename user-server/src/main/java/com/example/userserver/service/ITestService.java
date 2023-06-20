package com.example.userserver.service;

import com.example.userserver.dao.entity.Test;

public interface ITestService {


    Test getTestById(Integer id);

    void seataTest();

    void seataTest2();

}
