package com.example.commons;

import java.util.List;

public interface IAbstractService<T> {

    int create(T t);

    int deleteById(Integer id);

    int updateById(T t);

    List<T> listAll(T t);

    T getById(Integer id);

}
