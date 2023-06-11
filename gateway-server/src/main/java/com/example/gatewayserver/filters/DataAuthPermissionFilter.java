package com.example.gatewayserver.filters;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.utils.StringUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 * https://blog.csdn.net/weiwoyonzhe/article/details/90814680
 * @description:    数据权限过滤器
 * @author: mingyi ge
 * @date: 2022/8/17 17:05
 */
@Slf4j
public class DataAuthPermissionFilter implements GlobalFilter, Ordered {

    private static Joiner joiner = Joiner.on("");
    private Boolean encryptFlag = false;

    public DataAuthPermissionFilter() {
    }

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().getPath();
        ServerHttpResponse response = exchange.getResponse();
        HttpStatus statusCode = response.getStatusCode();

        if (true) {
            return chain.filter(exchange);
        }

        //
        if (Objects.equals(statusCode, HttpStatus.OK)) {
            String userId = "";
            return this.processResponse(exchange, chain, uri, userId, statusCode);
        } else {
            return chain.filter(exchange);
        }
    }


    public int getOrder() {
        return -1;
    }



    private Mono<Void> processResponse(ServerWebExchange exchange, GatewayFilterChain chain, String uri, String userId, HttpStatus statusCode) {
        log.info("数据权限过滤器处理响应数据!!");
        final ServerHttpResponse response = exchange.getResponse();

        ServerHttpResponseDecorator httpResponseDecorator = new ServerHttpResponseDecorator(response) {
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                String responseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                HttpHeaders responseHeaders = response.getHeaders();
                responseHeaders.add(HttpHeaders.CONTENT_TYPE, responseContentType);
                if (body instanceof Flux) {
                    if (!StringUtils.isEmpty(responseContentType) && responseContentType.contains("application/json")) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map((dataBuffers) -> {
                            List<String> list = Lists.newArrayList();
                            dataBuffers.forEach((dataBuffer) -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                list.add(new String(content, StandardCharsets.UTF_8));
                            });
                            String responseData = joiner.join(list);
//                            responseData = responseData.replaceAll("\n", "").replaceAll("\t", "");
                            log.info("获取到的完整的响应体为：{}", responseData);
                            String afterStr = "";
                            byte[] bytes = afterStr.getBytes(StandardCharsets.UTF_8);
                            response.getHeaders().setContentLength((long) bytes.length);
                            response.setStatusCode(statusCode);
                            log.info("处理响应数据成功!!");
                            return this.bufferFactory().wrap(bytes);
                        }));
                    }
                }
                return super.writeWith(body);
            }

        };
        return chain.filter(exchange.mutate().response(httpResponseDecorator).build());
    }


    /**
     * 递归待脱敏数据
     * 要处理数组的话  在外层for循环处理
     * @param json  json对象
     * @param desensitizationList   脱敏字段
     * @return  处理后的数据
     */
    private static JSONObject processData(Object json, List<String> desensitizationList) {
        //
        List<String> keyList = desensitizationList.stream().map(e -> e.split("-")[0]).collect(Collectors.toList());
        JSONObject jsonObject = null;

        //
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
            Iterator<String> iterator = jsonObject.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = jsonObject.get(key);

                if (value instanceof JSONObject) {
                    processData(value, desensitizationList);
                }

                if (value instanceof JSONArray) {
                    JSONArray tempJsonArray = (JSONArray) value;
                    for (int i = 0; i < tempJsonArray.size(); i++) {
                        processData(tempJsonArray.get(i), desensitizationList);
                    }
                }

                if (keyList.contains(key)) {
                    if (value instanceof String) {
                        String date = handlerDate((String) value, 1, 5, "*");
                        jsonObject.put(key, date);
                    } else {
                        jsonObject.put(key, null);
                    }
                }

            }
        }

        //
        if (json instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) json;
            for (int i = 0; i < jsonArray.size(); i++) {
                processData(jsonArray.get(i), desensitizationList);
            }
        }
        return jsonObject;
    }

    /**
     * 数据脱敏
     * @param str   待脱敏数据
     * @param startIdx  开始下标
     * @param endIdx    结束下标
     * @param replaceStr    替换字符
     * @return  脱敏后数据
     */
    private static String handlerDate(String str, int startIdx, int endIdx, String replaceStr) {
        if (startIdx == 0 && endIdx == 0) {
            return str;
        }
        if (startIdx > endIdx) {
            return str;
        }
        int strLength = str.length();
        if (startIdx > strLength || endIdx > strLength) {
            return str;
        }

        String beforeStr = str.substring(startIdx, endIdx);
        StringBuffer afterStr = new StringBuffer();
        for (int i = startIdx; i <= endIdx; i++) {
            afterStr.append(replaceStr);
        }
        return str.replaceFirst(beforeStr, afterStr.toString());
    }

}
