package com.example.analysisserver.kafka;

import com.example.pluginmq.producer.kafka.KafkaBusinessMessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerTest {

    @Autowired
    private KafkaTemplate<String, Object> producertemplate;

    @Autowired
    private KafkaBusinessMessageProducer kafkaBusinessMessageProducer;


    @Test
    public void async() {
        for (int i = 1; i < 11; i++) {
            producertemplate.send("topic1", 0, String.valueOf(i), "这是数据" + i);
        }
    }


    @Test
    public void async2() {
        for (int i = 1; i < 11; i++) {
            producertemplate.send("topic1", String.valueOf(i), "这是数据" + i).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onSuccess(SendResult<String, Object> sendResult) {
                    System.out.println("发送发生成功：" + sendResult.getProducerRecord().topic());
                }

                @Override
                public void onFailure(Throwable throwable) {
                    System.out.println("发送发生错误：" + throwable.getMessage());
                }
            });
        }
    }


    @Test
    public void sync() {
        try {
            for (int i = 1; i < 11; i++) {
                SendResult<String, Object> sendResult = producertemplate.send("topic1", String.valueOf(i), "这是数据" + i).get();
                System.out.println(sendResult.getProducerRecord().topic());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test3() {
        kafkaBusinessMessageProducer.sendBusinessMsg("topic1", null, "111");
    }

}
