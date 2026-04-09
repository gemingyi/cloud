package com.example.userserver.test;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 民政部2023行政区划爬取
 * HttpClient 4.5 低版本
 */
public class AreaTest {

    private static final String URL = "https://www.mca.gov.cn/mzsj/xzqh/2023/202301xzqh.html";

    // 区域实体
    @Getter
    @Setter
    static class Area {
        Integer id;
        Integer parentCode;
        Integer code;
        String name;
        Integer level;

        public Area(Integer id, Integer parentCode, Integer code, String name, Integer level) {
            this.id = id;
            this.parentCode = parentCode;
            this.code = code;
            this.name = name;
            this.level = level;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("开始爬取民政部行政区划数据...");
            String html = crawl(URL);

            System.out.println("开始解析数据...");
            List<Area> areaList = parse(html);
            System.out.println("解析完成，共：" + areaList.size() + " 条数据");

            System.out.println("开始入库...");
            saveToDatabase(areaList);
            System.out.println("✅ 全部保存到数据库成功！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 修复版：兼容所有JDK，无Lambda，编译通过
    private static CloseableHttpClient createSSLClient() throws Exception {
        // 传统匿名内部类，代替Lambda表达式
        TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                return true;
            }
        };
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build();
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        return HttpClients.custom().setSSLSocketFactory(factory).build();
    }


    /**
     * HttpClient 4.5 爬取页面
     */
    private static String crawl(String url) throws Exception {
        CloseableHttpClient httpClient = createSSLClient(); // 用跳过SSL的客户端
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);

        String html = "";
        try {
            HttpEntity entity = response.getEntity();
            html = EntityUtils.toString(entity, "UTF-8");
        } finally {
            response.close();
            httpClient.close();
        }
        return html;
    }

    /**
     * 解析省市区三级
     */
    private static List<Area> parse(String html) {
        List<Area> list = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements trs = doc.select("table tr");

        int id = 1;
        Integer lastProvince = null; // 保存上一级省code
        Integer lastCity = null;     // 保存上一级市code

        for (Element tr : trs) {
            Elements tds = tr.select("td");
            if (tds.size() < 3) continue;

            String codeStr = tds.get(1).text().trim();
            String name = tds.get(2).text().trim();

            if (codeStr.length() != 6) continue;

            try {
                int code = Integer.parseInt(codeStr);
                int level;
                Integer parentCode = 0;

                // 省级：结尾 0000
                if (code % 10000 == 0) {
                    level = 1;
                    parentCode = 0;
                    lastProvince = code;
                    lastCity = null;
                }
                // 市级：结尾 00
                else if (code % 100 == 0) {
                    level = 2;
                    parentCode = lastProvince;
                    lastCity = code;
                }
                // 区县级
                else {
                    level = 3;
                    parentCode = lastCity != null ? lastCity : lastProvince;
                }

                list.add(new Area(id++, parentCode, code, name, level));

            } catch (Exception ignored) {}
        }
        return list;
    }

    /**
     * 批量保存数据库
     */
    private static void saveToDatabase(List<Area> areaList) throws SQLException {
        for (Area area : areaList) {
            String sql = "INSERT INTO area_info(id, parent_code, code, name, level) VALUES ('#parentCode','#code','#name','#level')";
            sql = sql.replaceAll("#parentCode", String.valueOf(area.getParentCode()));
            sql = sql.replaceAll("#code", String.valueOf(area.getCode()));
            sql = sql.replaceAll("#name", area.getName());
            sql = sql.replaceAll("#level", String.valueOf(area.getLevel()));
            System.out.println(sql);
        }
    }
}