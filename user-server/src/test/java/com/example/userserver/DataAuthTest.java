package com.example.userserver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.commons.utils.AliJsonUtil;
import org.assertj.core.util.Lists;

import java.util.*;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/8/17 17:30
 */
public class DataAuthTest {

    public static void main(String[] args) {
        String jsonStr = "{\"errCode\":0,\"errMsg\":\"操作成功\",\"data\":{\"ruleId\":null,\"ruleName\":null,\"id\":14,\"companySpell\":\"zhihuirongzi\",\"companyName\":\"智汇融资租赁(深圳)有限公司\",\"companyCode\":\"91440300MA5DRR6FXK\",\"companyType\":3,\"companyAddress\":\"深圳市前海深港合作区前湾一路1号A栋201室\",\"legalName\":\"胡光铁\",\"legalNo\":\"88888888888888\",\"legalUrlJust\":\"https://alioss.woaizuji.com//fundFile/uploadImage/43026082241205327_a19793ce19c94ee491be283c799d2fb9.jpg\",\"legalUrlBack\":\"https://alioss.woaizuji.com//fundFile/uploadImage/43026997801633578_495f59684fb847049a48c3cce0a4fbd6.jpg\",\"contactsName\":\"胡光铁\",\"contactsPhone\":\"0755-86708309\",\"contactsAddress\":\"深圳市前海深港合作区前湾一路1号A栋201室\",\"contactsEmail\":\"finance@woaizuji.com\",\"isvOrgId\":\"123\",\"outTradeNo\":\"123\",\"outMerchantId\":\"123\",\"settlementType\":1,\"accountType\":2,\"bankName\":\"123\",\"bankNo\":\"123\",\"alipayName\":null,\"riskType\":2,\"fundOrderSend\":2,\"procedureType\":2,\"insure\":2,\"onChain\":2,\"repaymentSource\":1,\"orderApproval\":1,\"appIdCollection\":null,\"orderLoan\":1,\"ledgerAccountType\":3,\"compensatoryType\":1,\"compensatoryNm\":90,\"buyBackType\":1,\"buyBackNm\":90,\"repayType\":1,\"repayTypeValue\":0,\"enableFlag\":0,\"payerCode\":\"\",\"rentBackWarehouseCode\":\"W20211208095716000008\",\"defaultWarehouseCode\":\"W20211123160207000003\",\"serviceFeeType\":2,\"serviceFeePct\":0.1,\"buyBackHandleType\":2,\"buyBackWarehouseCode\":\"W20211208095716000008\",\"capitalCostPct\":0,\"operationId\":null,\"operationName\":null,\"capitalReqAddEntityReq\":{\"id\":3,\"prepareOrderQuota\":\"采购价*10000\",\"finalOrderQuota\":\"采购价*10000\",\"insureQuota\":\"\",\"orderQuota\":null,\"financingQuota\":null,\"retentionCost\":null,\"bond\":null,\"serviceCharge\":null},\"companyTemplateEntityList\":[{\"id\":1,\"templateNo\":\"template202104282102331\",\"formulaType\":1},{\"id\":2,\"templateNo\":\"template202104282102332\",\"formulaType\":2}],\"repaymentFormulaListReq\":null,\"supplierEntityReqList\":[]},\"success\":true}";
        List<String> desensitizationList = Lists.list("companyName", "templateNo", "finalOrderQuota");

        Map<String, String> replaceMap = new HashMap<>();
//        long start = System.currentTimeMillis();
        JSONObject jsonObject = recursiveUpdate(JSONObject.parse(jsonStr), desensitizationList, replaceMap);
//        System.out.println(System.currentTimeMillis() - start);
        System.out.println(AliJsonUtil.featureObjectToJsonStr(jsonObject));


        String jsonStr2 = "[{\"templateNo\":\"1\",\"formulaType\":1,\"id\":1},{\"templateNo\":\"2\",\"formulaType\":2,\"id\":2}]";
        List<String> desensitizationList2 = Lists.list("companyName", "templateNo", "finalOrderQuota");
        Map<String, String> replaceMap2 = new HashMap<>();
        JSONArray jsonArray = JSONArray.parseArray(jsonStr2);
        JSONArray resultJsonArray = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject tempJsonObject = recursiveUpdate(jsonArray.get(i), desensitizationList2, replaceMap2);
            resultJsonArray.add(tempJsonObject);
        }
        System.out.println(AliJsonUtil.featureObjectToJsonStr(resultJsonArray));
    }

    private static JSONObject recursiveUpdate(Object jsonStr, List<String> desensitizationList, Map<String, String> replaceMap) {
        JSONObject jsonObject = null;
        if (jsonStr instanceof JSONObject) {
            jsonObject = (JSONObject) jsonStr;
            Set<String> keySet = jsonObject.keySet();
            Iterator<String> keySetIterator = keySet.iterator();
            //
            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                Object value = jsonObject.get(key);

                // 是数组递归
                if (value instanceof JSONArray) {
                    recursiveUpdate(value, desensitizationList, replaceMap);
                }

                // 是对象
                if (value instanceof JSONObject) {
                    recursiveUpdate(value, desensitizationList, replaceMap);
                }

                // 是字段
                if (desensitizationList.contains(key)) {
                    replaceMap.put(key, "");
                    jsonObject .put(key, null);
                }
            }
        }

        if (jsonStr instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) jsonStr;
            for (int i = 0; i < jsonArray.size(); i++) {
                recursiveUpdate(jsonArray.get(i), desensitizationList, replaceMap);
            }
        }

        return jsonObject ;
    }


    public static JSONObject changeJsonObj(JSONObject jsonObj,Map<String, String> keyMap) {
        JSONObject resJson = new JSONObject();
        Set<String> keySet = jsonObj.keySet();
        for (String key : keySet) {
            String resKey = keyMap.get(key) == null ? key : keyMap.get(key);
            try {
                JSONObject jsonobj1 = jsonObj.getJSONObject(key);
                resJson.put(resKey, changeJsonObj(jsonobj1, keyMap));
            } catch (Exception e) {
                try {
                    JSONArray jsonArr = jsonObj.getJSONArray(key);
                    resJson.put(resKey, changeJsonArr(jsonArr, keyMap));
                } catch (Exception x) {
                    resJson.put(resKey, jsonObj.get(key));
                }
            }
        }
        return resJson;
    }

    public static JSONArray changeJsonArr(JSONArray jsonArr,Map<String, String> keyMap) {
        JSONArray resJson = new JSONArray();
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            resJson.add(changeJsonObj(jsonObj, keyMap));
        }
        return resJson;
    }


}
