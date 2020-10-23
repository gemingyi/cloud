package com.example.payserver.service;

import com.example.commons.utils.BeanUtil;
import com.example.commons.utils.QRBarCodeUtil;
import com.example.payserver.commons.weixin.MyConfig;
import com.example.payserver.commons.weixin.WXPay;
import com.example.payserver.commons.weixin.WXPayConstants;
import com.example.payserver.commons.weixin.WXPayUtil;
import com.example.payserver.commons.weixin.model.WXCallback;
import com.example.payserver.commons.weixin.model.WXCallbackResponse;
import com.example.payserver.commons.weixin.model.WXSAOMACallback;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.util.Map;


@Service
public class WeixinPayService {


    MyConfig config = new MyConfig();
    WXPay wxpay = new WXPay(config);

    public WeixinPayService() throws Exception {
    }

    /**
     * 扫码支付 模式一 生成商品二维码
     */
    public void nativeOneCreateQrCode() throws Exception {
        //生成二维码参数
        Map<String, String> params = new HashMap<>();
        params.put("time_stamp", "10");
        params.put("product_id", "商品ID");
        params = wxpay.fillRequestData(params);

        StringBuilder qrCode = new StringBuilder();
        qrCode.append("weixin://wxpay/bizpayurl?");
        qrCode.append("appid=").append(params.get("appid"));
        qrCode.append("&mch_id=").append(params.get("mch_id"));
        qrCode.append("&nonce_str=").append(params.get("nonce_str"));
        qrCode.append("&product_id=").append(params.get("product_id"));
        qrCode.append("&time_stamp=").append(params.get("time_stamp"));
        qrCode.append("&sign=").append(params.get("sign"));
        QRBarCodeUtil.generateQrCode(qrCode.toString());
    }

    /**
     * 扫码支付 模式一 回调
     */
//    @RequestMapping(value = "one/callback", produces = {"application/xml;charset=UTF-8"})
    public void nativeOneCallback(@RequestBody WXSAOMACallback wxsaomaCallback, HttpServletResponse response) throws Exception {
        //签名验证
        Map callbackMap = BeanUtil.Obj2Map(wxsaomaCallback);
        boolean isValid = wxpay.isPayResultNotifySignatureValid(callbackMap);
        String resultXmlStr = "";
        if (isValid) {
            //根据商品ID 设置业务价格
            String product_id = (String) callbackMap.get("product_id");
            //调用统一下单
            Map<String, String> params = new HashMap<>();
            params.put("body", "商品描述");
            params.put("out_trade_no", "商户订单号");
            params.put("total_fee", "标价金额");
            params.put("spbill_create_ip", "终端IP");
            params.put("notify_url", "回调通知地址");
            params.put("trade_type", "NATIVE");
            params.put("product_id", "***商品ID");
            Map<String, String> responseMap = wxpay.unifiedOrder(params);
            if (WXPayConstants.SUCCESS.equals(responseMap.get("return_code"))) {
                Map<String, String> prepayParams = new HashMap<>();
                prepayParams.put("return_code", "SUCCESS");
                String prepay_id = responseMap.get("prepay_id");
                prepayParams.put("prepay_id", prepay_id);
                if (WXPayConstants.SUCCESS.equals(responseMap.get("result_code"))) {
                    prepayParams.put("result_code", "SUCCESS");
                    prepayParams = wxpay.fillRequestData(prepayParams);
                    //通知微信 预支付
                    resultXmlStr = WXPayUtil.mapToXml(prepayParams);
                } else {
                    resultXmlStr = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[" + responseMap.get("err_code_des") + "]]></return_msg>" + "</xml> ";
                }
            } else {
                resultXmlStr = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[" + responseMap.get("return_msg") + "]]></return_msg>" + "</xml> ";
            }
        } else {
            resultXmlStr = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[签名错误]]></return_msg>" + "</xml> ";
        }
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resultXmlStr.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 扫码支付 模式二 统一下单
     * https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
     */
    public void nativeTwoCreateOrder() throws Exception {
        //统一下单接口参数
        Map<String, String> params = new HashMap<>();
        //商品描述
        params.put("body", "商品描述");
        //商户订单号
        params.put("out_trade_no", "商户订单号");
        //货币类型
//        params.put("fee_type", "CNY");
        //标价金额
        params.put("total_fee", "标价金额");
        //终端IP
        params.put("spbill_create_ip", "终端IP");
        //回调通知地址
        params.put("notify_url", "回调通知地址");
        //交易类型 JSAPI -JSAPI支付|NATIVE -Native支付|APP -APP支付
        params.put("trade_type", "NATIVE");

        Map<String, String> responseMap = wxpay.unifiedOrder(params);
        if (WXPayConstants.SUCCESS.equals(responseMap.get("return_code"))) {
            if (WXPayConstants.SUCCESS.equals(responseMap.get("result_code"))) {
                //转换短链接
                responseMap = wxpay.shortUrl(responseMap);
                String urlCode = (String) responseMap.get("code_url");
                // 生成二维码
//                ZxingUtils.getQRCodeImge(urlCode, 256, responseMap.get("code_url"));
            } else {
                String errCodeDes = responseMap.get("err_code_des");
            }
        } else {
            String returnMsg = responseMap.get("return_msg");
        }
    }


    /**
     * 支付结果回调
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7&index=8
     */
//    @RequestMapping(value = "/callback", produces = {"application/xml;charset=UTF-8"})
    public WXCallbackResponse callback(@RequestBody WXCallback callback) throws Exception {
        WXCallbackResponse wxCallbackResponse = new WXCallbackResponse();
        //签名验证
        Map callbackMap = BeanUtil.Obj2Map(callback);
        boolean isValid = wxpay.isPayResultNotifySignatureValid(callbackMap);
        if (isValid) {
            String returnCode = callback.getReturn_code();
            if (WXPayConstants.SUCCESS.equalsIgnoreCase(returnCode)) {
                String resultCode = callback.getResult_code();
                if (WXPayConstants.SUCCESS.equals(resultCode)) {
                    wxCallbackResponse.setReturn_code(WXPayConstants.SUCCESS);
                    wxCallbackResponse.setReturn_msg("OK");
                } else {
                    wxCallbackResponse.setReturn_code(WXPayConstants.FAIL);
                    wxCallbackResponse.setReturn_msg(callback.getErr_code());
                }
            } else {
                wxCallbackResponse.setReturn_code(WXPayConstants.FAIL);
                wxCallbackResponse.setReturn_msg(callback.getReturn_msg());
            }
        } else {
            wxCallbackResponse.setReturn_code(WXPayConstants.FAIL);
            wxCallbackResponse.setReturn_msg("签名错误");
        }
        return wxCallbackResponse;
    }

    /**
     * 查询订单
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2
     * SUCCESS—支付成功，REFUND—转入退款
     * NOTPAY—未支付， CLOSED—已关闭
     * REVOKED—已撤销（付款码支付）,USERPAYING--用户支付中（付款码支付）
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    public void queryOrder() throws Exception {
        //查询订单接口参数
        Map<String, String> params = new HashMap<>();
        //微信订单号\商户订单号  二选一
//        params.put("transaction_id", "");
        params.put("out_trade_no", "");
        Map<String, String> responseMap = wxpay.orderQuery(params);
        if (WXPayConstants.SUCCESS.equals(responseMap.get("return_code"))) {
            if (WXPayConstants.SUCCESS.equals(responseMap.get("result_code"))) {
                responseMap.get("trade_state");
            } else {
                String errCodeDes = responseMap.get("err_code_des");
            }
        } else {
            String returnMsg = responseMap.get("return_msg");
        }
    }

}
