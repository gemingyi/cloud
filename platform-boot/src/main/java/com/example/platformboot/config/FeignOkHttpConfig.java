package com.example.platformboot.config;

import com.google.common.base.Stopwatch;
import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/***
 *
 * @Description: ok-feign3配置
 * https://blog.csdn.net/chinasi2012/article/details/105891207
 * https://blog.csdn.net/chinasi2012/article/details/126662922
 * @author mingyi ge
 * @date 2020/12/17 20:35
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {

    private Logger log = LoggerFactory.getLogger(FeignOkHttpConfig.class);


    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES))
//                .addInterceptor(new LoggingInterceptor())
                .build();
    }


    /**
     * feignOkHttp调用日志拦截器
     */
    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Stopwatch start = Stopwatch.createStarted();
            Request request = chain.request();
            long thread = Thread.currentThread().getId();
            log.info("[{}] feign RPC request 请求方式=[{}],URL=[{}],请求头=[{}],请求参数=[{}]",
                    thread, request.method(), request.url(), request.headers(), request.body());

            Response response = chain.proceed(request);
            long millis = start.stop().elapsed(TimeUnit.MILLISECONDS);
            log.info("[{}] feign RPC response 耗时=[{}],响应参数=[{}]", thread, millis, response.body());

            return response;
        }
    }

}