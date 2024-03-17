package com.example.pluginnetty.netty.handler;

import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/11/4 16:51
 */
@Slf4j
public class SslFactory {

    private static SslContext sslContext;

    private static List<String> cipherList = new ArrayList<>();
    static {
        cipherList.add("ECDHE-RSA-AES128-SHA");
        cipherList.add("ECDHE-RSA-AES256-SHA");
        cipherList.add("AES128-SHA");
        cipherList.add("AES256-SHA");
        cipherList.add("DES-CBC3-SHA");
    }


    public static SslContext createSslContext(String certFilePath, String keyFilePath) {
        if (null == sslContext) {
            synchronized(SslFactory.class) {
                if (null == sslContext) {
                    File certFile = new File(certFilePath);
                    File keyFile = new File(keyFilePath);//此处需要PKS8编码的.key后缀文件

                    try {
                        sslContext = SslContextBuilder.forServer(certFile, keyFile)
                                .sslProvider(SslProvider.OPENSSL)
                                .clientAuth(ClientAuth.NONE)
                                .ciphers(cipherList)
                                .build();
                    } catch (SSLException e) {
                        log.error("SSL错误：", e);
                    }
                }
            }
        }
        return sslContext;
    }

}
