package com.example.userserver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.assertj.core.util.Lists;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/8/17 17:30
 */
public class DataAuthTest {

    public static void main(String[] args) {
        String jsonStr = "{\"errCode\":0,\"errMsg\":\"操作成功\",\"data\":{\"ruleId\":null,\"ruleName\":null,\"id\":14,\"companySpell\":\"zhihuirongzi\",\"companyName\":\"智汇融资租赁(深圳)有限公司\",\"companyCode\":\"91440300MA5DRR6FXK\",\"companyType\":3,\"companyAddress\":\"深圳市前海深港合作区前湾一路1号A栋201室\",\"legalName\":\"胡光铁\",\"legalNo\":\"88888888888888\",\"legalUrlJust\":\"https://alioss.woaizuji.com//fundFile/uploadImage/43026082241205327_a19793ce19c94ee491be283c799d2fb9.jpg\",\"legalUrlBack\":\"https://alioss.woaizuji.com//fundFile/uploadImage/43026997801633578_495f59684fb847049a48c3cce0a4fbd6.jpg\",\"contactsName\":\"胡光铁\",\"contactsPhone\":\"0755-86708309\",\"contactsAddress\":\"深圳市前海深港合作区前湾一路1号A栋201室\",\"contactsEmail\":\"finance@woaizuji.com\",\"isvOrgId\":\"123\",\"outTradeNo\":\"123\",\"outMerchantId\":\"123\",\"settlementType\":1,\"accountType\":2,\"bankName\":\"123\",\"bankNo\":\"123\",\"alipayName\":null,\"riskType\":2,\"fundOrderSend\":2,\"procedureType\":2,\"insure\":2,\"onChain\":2,\"repaymentSource\":1,\"orderApproval\":1,\"appIdCollection\":null,\"orderLoan\":1,\"ledgerAccountType\":3,\"compensatoryType\":1,\"compensatoryNm\":90,\"buyBackType\":1,\"buyBackNm\":90,\"repayType\":1,\"repayTypeValue\":0,\"enableFlag\":0,\"payerCode\":\"\",\"rentBackWarehouseCode\":\"W20211208095716000008\",\"defaultWarehouseCode\":\"W20211123160207000003\",\"serviceFeeType\":2,\"serviceFeePct\":0.1,\"buyBackHandleType\":2,\"buyBackWarehouseCode\":\"W20211208095716000008\",\"capitalCostPct\":0,\"operationId\":null,\"operationName\":null,\"capitalReqAddEntityReq\":{\"id\":3,\"prepareOrderQuota\":\"采购价*10000\",\"finalOrderQuota\":\"采购价*10000\",\"insureQuota\":\"\",\"orderQuota\":null,\"financingQuota\":null,\"retentionCost\":null,\"bond\":null,\"serviceCharge\":null},\"companyTemplateEntityList\":[{\"id\":1,\"templateNo\":\"template202104282102331\",\"formulaType\":1},{\"id\":2,\"templateNo\":\"template202104282102332\",\"formulaType\":2}],\"repaymentFormulaListReq\":null,\"supplierEntityReqList\":[]},\"success\":true}";
        List<String> desensitizationList = Lists.list("data.companyName-0-5", "templateNo-0-5", "finalOrderQuota-0-5");

//        Map<String, String> replaceMap = new HashMap<>();
//        long start = System.currentTimeMillis();
//        recursiveUpdate(jsonStr, desensitizationList, replaceMap);
//        System.out.println(System.currentTimeMillis() - start);

//        JSONArray jsonArray = new JSONArray();
//        jsonArray.add(JSONObject.parse(jsonStr));
//        String processFiled = "companyName-2-5";
//        String[] split = processFiled.split("-");
//        JSONArray jsonArray1 = recursiveUpdate(jsonArray, split, "");
//        System.out.println(jsonArray1);


        JSONObject jsonObject = processData("", JSONObject.parse(jsonStr), desensitizationList);
        System.out.println(jsonObject);
    }


    private static JSONObject processData(String keyPre, Object json, List<String> desensitizationList) {
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
                    processData(key.concat(".") , value, desensitizationList);
                }

                if (value instanceof JSONArray) {
                    JSONArray tempJsonArray = (JSONArray) value;
                    for (int i = 0; i < tempJsonArray.size(); i++) {
                        processData(key.concat("."), tempJsonArray.get(i), desensitizationList);
                    }
                }

                if (keyList.contains(keyPre.concat(key))) {
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
                processData("", jsonArray.get(i), desensitizationList);
            }
        }
        return jsonObject;
    }


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


    private static JSONArray recursiveUpdate(JSONArray jsonArray, String[] split, String listKey) {

        for (int i = 0; i < jsonArray.size(); ++i) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Set<String> sets = jsonObject.keySet();
            Iterator var7 = sets.iterator();

            String hidden;
            JSONArray jsonArray1x;
            while (var7.hasNext()) {
                hidden = (String) var7.next();
                String valueStr = jsonObject.getString(hidden);

                try {
                    JSONArray jsonArrayValue = JSONObject.parseArray(valueStr);
                    listKey = hidden;
                    jsonArray1x = recursiveUpdate(jsonArrayValue, split, hidden);
                    jsonObject.put(hidden, jsonArray1x);
                } catch (Exception var14) {
                    try {
                        JSONObject jsonObject1 = JSONObject.parseObject(valueStr);
                        JSONArray jsonArray1 = new JSONArray();
                        jsonArray1.add(jsonObject1);
                        jsonArray1 = recursiveUpdate(jsonArray1, split, listKey);
                        jsonObject.put(hidden, jsonArray1.get(0));
                    } catch (Exception var13) {
                    }

                }
            }

            if (jsonObject.containsKey(split[0])) {
                StringBuffer value = new StringBuffer(jsonObject.getString(split[0]));
                hidden = jsonObject.getString("hiddenProcessData");
                List<String> list = null;
                if (!StringUtils.isEmpty(hidden)) {
                    if (value.indexOf("*") < 0) {
                        list = JSONObject.parseArray(hidden, String.class);
                        ((List) list).add(split[0] + "-" + value);
                    }
                } else {
                    list = new ArrayList();
                    ((List) list).add(split[0] + "-" + value);
                }

                jsonObject.put("hiddenProcessData", list);
                value = processResponseData(value, split);
                jsonArray1x = null;
                if (listKey != null && listKey != "") {
                    jsonArray1x = jsonObject.getJSONArray(listKey);
                    jsonObject.remove(listKey);
                }

                Object updateAfterJson = jsonObject.put(split[0], value.toString());
                if (jsonArray1x != null && jsonArray1x.size() > 0) {
                    jsonObject.put(listKey, jsonArray1x);
                }

                jsonArray.set(i, updateAfterJson);
            }
        }

        return jsonArray;

    }


    private static StringBuffer processResponseData(StringBuffer value, String[] split) {
        Integer limit;
        if (split.length == 5) {
            Integer limitX = Integer.parseInt(split[2]);
            limit = Integer.parseInt(split[4]);
            value = desensitization(value, limitX, limit);
        } else if (split.length == 3) {
            String str = split[1];
            limit = Integer.parseInt(split[2]);
            if (str.equals("X")) {
                value = desensitization(value, limit, (Integer) null);
            } else {
                value = desensitization(value, (Integer) null, limit);
            }
        }

        return value;
    }

    private static StringBuffer desensitization(StringBuffer value, Integer limitX, Integer limitY) {
        int length = value.length();
        int beforeIndex = 0;
        int endIndex = length;
        int m = 0;
        if (limitX != null && limitX > 0 && limitX <= length) {
            beforeIndex = limitX - 1;
            m = length - limitX + 1;
        }

        if (limitY != null && limitY > 0 && limitY <= length) {
            m = limitY;
            endIndex = limitY;
            if (limitX != null && limitX > 0 && limitX < limitY) {
                m = limitY - limitX + 1;
            }
        }

        String str = "";
        if (m > 0) {
            for (int i = 0; i < m; ++i) {
                str = str + "*";
            }
        }

        if (str.length() > 0) {
            value = value.replace(beforeIndex, endIndex, str);
        }

        return value;
    }

}
