package com.example.pluginnetty.netty.codec;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/4 23:48
 */
public class Message {
    private Long id;

    private String name;

    public Message(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                '}';
    }
}
