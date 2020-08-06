package com.example.payserver.commons.weixin.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class WXSAOMACallback {


    //公众账号ID
    private String appid;

    //用户标识
    private String openid;

    //商户号
    private String mch_id;

    //是否关注公众账号
    private String is_subscribe;

    //随机字符串
    private String nonce_str;

    //商品ID
    private String product_id;

    //签名
    private String sign;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
