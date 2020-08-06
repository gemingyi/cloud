package com.example.plugines.commons.utils;

import com.example.plugines.commons.Enum.FieldType;
import com.example.plugines.commons.annotation.Document;
import com.example.plugines.commons.annotation.Field;
import com.example.plugines.commons.model.ElasticEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;

import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * elasticsearch工具类
 * Created by gmy on 2018/7/2.
 */
@Component
public class NativeESUtils {

    public TransportClient initClient(String ip, String clusterName, int port) {
        TransportClient client = null;
        try {
            Settings settings = Settings.builder()
//                    .put("xpack.security.user", "elastic:changeme")
                    .put("cluster.name", clusterName)
                    .put("client.transport.sniff", true)
                    .build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(ip), port));
            System.out.println(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    public TransportClient initClusterClient(String ipsStr, String clusterName, int port) {
        TransportClient client = null;
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", clusterName)
                    .put("client.transport.sniff", true)
                    .build();
            client = new PreBuiltTransportClient(settings);
            String ips[] = ipsStr.split("&");
            //添加集群IP列表
            for (String ip : ips) {
                client.addTransportAddress(new TransportAddress(InetAddress.getByName(ip), port));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    /**
     * 关闭连接
     */
    public void close(TransportClient client) {
        if (client != null) {
            client.close();
        }
    }


    /**
     * 创建索引与mapping模板
     */
    public void createIndexAndCreateMapping(TransportClient client, Class<? extends ElasticEntity> clazz) {
        Document document = clazz.getAnnotation(Document.class);
        String indexName = document.indexName();
        boolean isExists = isExistsIndex(client, indexName);
        //是否存在index
        CreateMappingUtil createMappingUtil = new CreateMappingUtil();
        if (!isExists) {
            createMappingUtil.createIndexAndMapping(clazz, client, Boolean.TRUE);
        } else {
            createMappingUtil.createIndexAndMapping(clazz, client, Boolean.FALSE);
        }
    }

    /**
     * 判断索引是否存在
     *
     * @param client    客户端
     * @param indexName 索引名称
     * @return 是否存在
     */
    public boolean isExistsIndex(TransportClient client, String indexName) {
        IndicesExistsResponse response = client.admin().indices()
                .exists(new IndicesExistsRequest().indices(indexName)).actionGet();
        return response.isExists();
    }

    /**
     * 创建索引工具类
     */
    private static class CreateMappingUtil {

        public void createIndexAndMapping(Class<?> clazz, TransportClient client, Boolean isCreateIndex) {
            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

            Document document = clazz.getAnnotation(Document.class);
            String indexName = document.indexName();
            String type = document.type();

            String settingStr = null;
            com.example.plugines.commons.annotation.Settings settings = clazz.getAnnotation(com.example.plugines.commons.annotation.Settings.class);
            if (null != settings) {
                settingStr = SettingsPropertiesUtils.getProperty(settings.settingJsonKey());
            }
            //索引不存在，则创建索引
            if (Boolean.TRUE.equals(isCreateIndex)) {
                if (null == settingStr) {
                    client.admin().indices().prepareCreate(indexName).execute().actionGet();
                } else {
                    client.admin().indices().prepareCreate(indexName).setSettings(settingStr, XContentType.JSON).execute().actionGet();
                }
            }
            XContentBuilder builder = builderMapping(indexName, fields, null);
            //创建mapping
            PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(type).source(builder);
            client.admin().indices().putMapping(mapping).actionGet();
        }

        /**
         * 递归 创建mapping
         *
         * @param indexName 索引名称
         * @param fields    全部属性
         * @param builder   mapping拼凑构造器
         * @return XContentBuilder
         */
        private XContentBuilder builderMapping(String indexName, java.lang.reflect.Field[] fields, XContentBuilder builder) {
            try {
                if (builder == null) {
                    builder = jsonBuilder().startObject().startObject("properties");
                } else {
                    builder = builder.startObject(indexName).field("type", "nested").startObject("properties");
                }
                for (java.lang.reflect.Field field : fields) {
                    if (field.isAnnotationPresent(Field.class)) {
                        Field aField = field.getAnnotation(Field.class);
                        if (FieldType.Nested.equals(aField.type())) {
                            //嵌套集合
                            Class<?> c = field.getType();
                            if (c.isAssignableFrom(List.class)) {
                                Type fc = field.getGenericType();
                                if (fc instanceof ParameterizedType) {
                                    ParameterizedType pt = (ParameterizedType) fc;
                                    Class<?> subClazz = (Class<?>) pt.getActualTypeArguments()[0];
                                    String subIndexName = field.getName();
                                    java.lang.reflect.Field[] subFields = subClazz.getDeclaredFields();
                                    builder = builderMapping(subIndexName, subFields, builder);
                                }
                            }
                        } else {
                            builder = builderField(builder, field, aField);
                        }

                    }
                }
                builder.endObject().endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder;
        }

        /**
         * 生成 字段属性
         *
         * @param builder mapping拼凑构造器
         * @param field   单个属性
         * @param aField  注解属性
         * @return XContentBuilder
         */
        private XContentBuilder builderField(XContentBuilder builder, java.lang.reflect.Field field, Field aField) {
            try {
                FieldType type = aField.type();
                builder.startObject(field.getName()).field("type", type.toString().toLowerCase());
                if (aField.store()) {
                    builder.field("store", aField.store());
                }
                if (!StringUtils.isEmpty(aField.analyzer())) {
                    builder.field("analyzer", aField.analyzer());
                }
                if (!StringUtils.isEmpty(aField.searchAnalyzer())) {
                    builder.field("search_analyzer", aField.searchAnalyzer());
                }
                if (!StringUtils.isEmpty(aField.fields())) {
                    String fieldsStr = aField.fields();
                    Map<String, Object> map = new Gson().fromJson(fieldsStr, new TypeToken<HashMap<String, Object>>() {
                    }.getType());
                    builder.field("fields", map);
                }
                builder.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder;
        }

    }

}
