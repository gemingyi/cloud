package com.example.commons.utils.json.desensitization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;


/**
 * https://blog.csdn.net/u012954706/article/details/97769715
 * https://www.yht7.com/news/161565
 * @description:
 * @author: mingyi ge
 * @date: 2022/8/18 17:45
 */
public class JsonUtils {

    private static ObjectMapper objectMapperSensitivity = new ObjectMapper();

//    private JsonUtils() {
//        //脱敏日志创建
//        objectMapperSensitivity.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        objectMapperSensitivity.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        objectMapperSensitivity.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapperSensitivity.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        //脱敏
//        objectMapperSensitivity.setSerializerFactory(objectMapperSensitivity.getSerializerFactory().withSerializerModifier(new SensitiveSerializerModifier()));
//    }


    public static void main(String[] args) {
        JsonUtils.DemoDTO demoDTO = new DemoDTO();
        demoDTO.setId(1L);
        demoDTO.setPhone("18774987061");
        demoDTO.setEmail("915674992@qq.com");
        String s = toJsonStringWithSensitivity(demoDTO);
        System.out.println(s);
    }


    /**
     * 对象转Json格式字符串----脱敏处理(包含map)
     *
     * @return
     */
    public static String toJsonStringWithSensitivity(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;

            map.forEach((key, value) -> {
                if (key instanceof String) {
                    String keyString = key.toString();
                    String s = dealSensitivity(keyString, value.toString());
                    map.put(keyString, s);
                }
            });
        }

        try {
            return objectMapperSensitivity.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String dealSensitivity(String mapkey, String mapValue) {
        //正则匹配
        for (Map.Entry<String, SensitiveTypeEnum> entry : SensitivityConstants.sensitivityRules.entrySet()) {
            String rule = entry.getKey();
            int length = rule.length();
            int propLen = mapkey.length();
            if (mapkey.length() < length) {
                continue;
            }
            int temp = rule.indexOf("*");
            String key = null;
            String substring = null;
            if (temp >= 0) {
                if (temp < (length >> 2)) {
                    key = rule.substring(temp + 1, length);
                    substring = mapkey.substring(propLen - key.length(), propLen);
                } else {
                    key = rule.substring(0, temp);
                    substring = mapkey.substring(0, temp);
                }
                if (substring.equals(key)) {
                    return SensitiveInfoUtils.sensitveValue(entry.getValue(), mapValue);
                }
            } else if (rule.equals(mapkey)) {
                return SensitiveInfoUtils.sensitveValue(entry.getValue(), mapValue);
            }
        }
        return mapValue;
    }


    @Getter
    @Setter
    static
    class DemoDTO {
        private Long id;

        private String name;

        private Integer age;

        @SensitiveInfo(SensitiveTypeEnum.MOBILE_PHONE)
        private String phone;

        @SensitiveInfo(SensitiveTypeEnum.EMAIL)
        private String email;

        private String delFlag;

        private Long createUser;

        private String createName;

        private Date createTime;

        private Long updateUser;

        private String updateName;

        private Date updateTime;
    }

}

