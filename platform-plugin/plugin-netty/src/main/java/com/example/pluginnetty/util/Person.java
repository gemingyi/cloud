package com.example.pluginnetty.util;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 23:48
 */
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }

}
