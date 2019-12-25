package com.seasoning.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WechatApiUtil {

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.appSecret}")
    private String appSecret;

    private String CODE_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";


    public String code2Session(String code){

        Map<String, String> param = new HashMap<>();
        param.put("appid", appId);
        param.put("secret", appSecret);
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        return HttpUtil.doGet(CODE_SESSION_URL, param);
    }
}
