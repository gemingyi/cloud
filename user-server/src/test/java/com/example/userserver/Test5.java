package com.example.userserver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * https://www.it610.com/article/1288854075256938496.htm
 * https://www.zhihu.com/question/23486749
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/20 20:11
 */
public class Test5 {

    public static void main(String[] args) {

//        FelordPublisher felordPublisher = new FelordPublisher();
//        felordPublisher.setBroker(Broker.getInstance());
//        // 发布了一篇文章
//        felordPublisher.publish("Java", "发布订阅模式相关探讨");
//        // 然而没有订阅者
//        System.out.println("然而没有订阅者");
//
//        System.out.println("张三订阅了 Java，李四订阅了Python");
//        Broker.getInstance().addSub("Java", new SomeSubscriber("张三"));
//        Broker.getInstance().addSub("Python", new SomeSubscriber("李四"));
//
//        felordPublisher.publish("Java", "Java 真的很难学吗");
//        System.out.println("王五 订阅了Java");
//        Broker.getInstance().addSub("Java", new SomeSubscriber("王五"));
//        felordPublisher.publish("Java", "新鲜资讯可访问felord.cn");
//        // 发布了Python 文章
//        felordPublisher.publish("Python", "Python 一天入门");

        int[] nums = {-2, -1, 2, 1};
        maxSubArrayLen(nums, 1);
    }

    public static int maxSubArrayLen(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int maxLen = 0;
        for (int i = 0, sum = 0; i < nums.length; i++) {
            sum += nums[i];
            if (map.containsKey(sum - k)) {
                maxLen = Math.max(maxLen, i - map.get(sum - k));
            } else {
                map.putIfAbsent(sum, i);
            }
        }
        return maxLen;
    }

}

interface Publisher {
    /**
     * 向某个主题发布事件.
     *
     * @param topic the topic
     * @param event the event
     */
    void publish(String topic, String event);
}


interface Subscriber {

    /**
     * On event.
     *
     * @param event the event
     */
    void onEvent(String event);

}

class Broker {

    private final Map<String, Set<Subscriber>> subscribers = new ConcurrentHashMap<>();

    private Broker() {
    }

    private static class Instance {
        private static final Broker INSTANCE = new Broker();
    }

    /**
     * 获取单例.
     *
     * @return the instance
     */
    public static Broker getInstance() {
        return Instance.INSTANCE;
    }

    /**
     * 对主题进行订阅.
     *
     * @param topic      the topic
     * @param subscriber the subscriber
     * @return the boolean
     */
    public boolean addSub(String topic, Subscriber subscriber) {

        if (subscribers.get(topic) == null) {
            Set<Subscriber> objects = new HashSet<>();
            objects.add(subscriber);
            subscribers.put(topic, objects);
        }

        return subscribers.get(topic).add(subscriber);
    }

    /**
     * 取消订阅.
     *
     * @param topic      the topic
     * @param subscriber the subscriber
     * @return the boolean
     */
    public boolean removeSub(String topic, Subscriber subscriber) {
        if (subscribers.get(topic) == null) {
            return true;
        }
        return subscribers.get(topic).remove(subscriber);
    }


    /**
     * 迭代推送事件给订阅者.
     *
     * @param topic the topic
     * @param event the event
     */
    public void broadcasting(String topic, String event) {
        subscribers.getOrDefault(topic, new HashSet<>()).forEach(subscriber -> subscriber.onEvent(event));
    }

}

class FelordPublisher implements Publisher {

    private Broker broker;

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    @Override
    public void publish(String topic, String event) { //这里调用Broker
        System.out.println("码农小胖哥在 " + topic + " 中发布了一个 " + event + "的事件");
        broker.broadcasting(topic, event);
    }

}

class SomeSubscriber implements Subscriber {
    private final String name;

    public SomeSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(String event) {
        System.out.println("粉丝 " + name + "接收到了事件 " + event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SomeSubscriber that = (SomeSubscriber) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}