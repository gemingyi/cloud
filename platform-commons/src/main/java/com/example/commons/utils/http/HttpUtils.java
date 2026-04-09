package com.example.commons.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.util.Map;

public class HttpUtils {

    private static CloseableHttpClient getHttpClient(boolean proxyFlag, String proxyHost, Integer proxyPort, boolean sslFlag) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (proxyFlag) {
            RequestConfig requestConfig = RequestConfig.custom()
                    //设置套接字超时 连接连接超时 请求超时
                    .setSocketTimeout(10000).setConnectTimeout(10000).setConnectionRequestTimeout(10000)
                    //设置代理
                    .setProxy(new HttpHost(proxyHost, proxyPort))
                    .build();
            httpClientBuilder.setDefaultRequestConfig(requestConfig);
        }
        if (sslFlag) {
            SSLContext sslContext = null;
            try {
                sslContext = SSLContextBuilder.create().loadTrustMaterial(null, (chain, authType) -> true).build();
                SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
                httpClientBuilder.setSSLSocketFactory(sslFactory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return httpClientBuilder.build();
    }

    /**
     * responseCode: java.net.HttpURLConnection
     * header: org.apache.http.protocol.HTTP、org.springframework.http.HttpHeaders、org.springframework.http.MediaType
     */
    public static void doPost(String url, Map<String, String> headers, String body) throws Exception {
        CloseableHttpClient httpClient = getHttpClient(false, null, null, false);
        //设置请求头
        HttpPost post = new HttpPost(url);
        headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        for (String key : headers.keySet()) {
            post.setHeader(key, headers.get(key));
        }
        //设置请求体
        // 1.FormEntity form表单键值对参数
//        List<NameValuePair> pairs = new ArrayList<>();
//        pairs.add(new BasicNameValuePair("key","value"));
//        HttpEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs);
        // 2.StringEntity 字符串参数
        HttpEntity entity = new StringEntity(body, "UTF-8");
        //包括但不限于以下许多种HttpEntity
        // InputStreamEntity
        // FileEntity / NFileEntity
        // GzipCompressingEntity / GzipDecompressingEntity
        post.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(post);
        String respEntity = EntityUtils.toString(response.getEntity());
        //关闭连接
        httpClient.close();
        response.close();
    }

    public static void doGet(String url, Map<String, String> headers, String body, Long timeout) throws Exception {
        CloseableHttpClient httpClient = getHttpClient(false, null, null, false);
        HttpGet get = new HttpGet(url);
        for (String key : headers.keySet()) {
            get.setHeader(key, headers.get(key));
        }
        CloseableHttpResponse response = httpClient.execute(get);
        String respEntity = EntityUtils.toString(response.getEntity());
        httpClient.close();
        response.close();
    }

}
