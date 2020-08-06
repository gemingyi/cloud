package com.example.analysisserver.kafka;

import com.example.analysisserver.model.Notice;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderProducerTest {

    @Autowired
    private KafkaTemplate<String, Object> producertemplate;


    @Test
    public void async2() {
        String[] urls = new String[]{
                "http://www.zzzyjy.cn/016/016001/016001001/1.html",
                "http://www.zzzyjy.cn/016/016002/016002001/1.html",
                "http://www.zzzyjy.cn/016/016003/016003001/1.html",
                "http://www.zzzyjy.cn/016/016004/016004001/1.html",
                "http://www.zzzyjy.cn/016/016005/016005001/1.html"
        };
        for (int i = 0; i < urls.length; i++) {
            int page = 1;
            Connection conn ;
            Document doc;
            String url = urls[i];
            try {
                for (int pagelist = 1; pagelist <= page; pagelist++) {

                    if (pagelist > 1) {
                        url = urls[i].substring(0, urls[i].lastIndexOf("/") + 1) + pagelist + ".html";
                    }
                    System.out.println("第" + pagelist + "页");
                    conn = Jsoup.connect(url).userAgent("Mozilla").timeout(1000 * 60).ignoreHttpErrors(true);
                    doc = conn.get();
                    if (page == 1) {
                        //获取总页数
                        String pageCont = doc.select("#index").first().text().trim();
                        page = Integer.valueOf(pageCont.substring(pageCont.indexOf("/") + 1));
                        System.out.println("总" + page + "页");
                    }
                    Elements trs = doc.select(".ewb-info-list").first().select("[class=ewb-list-node clearfix]");
                    for (int row = 0; row < trs.size(); row++) {
                        Thread.sleep(2000);
                        Notice notice = new Notice();
                        String contUrl = trs.get(row).select("a").first().absUrl("href");
                        String date = trs.get(row).select(".ewb-list-date").text().trim();

                        notice.setPublishDate(date);
                        notice.setUrl(contUrl);

                        Document docCount = Jsoup.parse(new URL(contUrl).openStream(), "UTF-8", contUrl);
                        String title = docCount.select(".ewb-article-hd").first().text().trim();
                        String content = docCount.select("[class=ewb-article-content con]").html();
                        notice.setTitle(title);
                        notice.setContent(content);

                        producertemplate.send("topic1", UUID.randomUUID().toString(), notice).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
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
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
