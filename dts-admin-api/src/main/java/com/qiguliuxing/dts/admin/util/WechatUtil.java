package com.qiguliuxing.dts.admin.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class WechatUtil {

    @Autowired
    private RestTemplate restTemplate;

   @Value("wx.access-token")
   private String accessToken;

    @Value("wx.app-id")
    private String appId;

    @Value("wx.app-secret")
    private String appSecret;

    public JSONObject getAccessToken(){
        Map<String,String> map = new HashMap<>();
        map.put("grant_type","client_credential");
        map.put("appid","appId");
        map.put("secret","appSecret");
        String forObject = restTemplate.getForObject(accessToken, String.class,map);
        JSONObject jsonObject = JSONObject.parseObject(forObject);
        System.out.println("微信发布订阅返回的token："+jsonObject);
        return jsonObject;
    }
}
