package com.example.analysisserver.disruptor.Model;

/**
 * Created by Administrator on 2018/4/15.
 */
public class Orange {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Orange(){}

    public Orange(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + id;
    }
}