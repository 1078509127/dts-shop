package com.qiguliuxing.dts.admin.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class WechatUtil {

    @Autowired
    private RestTemplate restTemplate;

   @Value("${dts.wx.access-token}")
   private String accessToken;

    @Value("${dts.wx.app-id}")
    private String appId;

    @Value("${dts.wx.app-secret}")
    private String appSecret;

    public  String getAccessToken(){
        Map<String,String> map = new HashMap<>();
        map.put("grant_type","client_credential");
        map.put("appid",appId);
        map.put("secret",appSecret);
        String forObject = restTemplate.getForObject(accessToken+"?grant_type=client_credential&appid="+appId+"&secret="+appSecret, String.class);
        JSONObject jsonObject = JSONObject.parseObject(forObject);
        String token = jsonObject.getString("access_token");
        if (StringUtils.isEmpty(accessToken)) {
            System.out.println("获取access token失败"+jsonObject.getString("errmsg"));
            throw new ApiException("获取access token失败");
        } else {
            return token;
        }
    }
}
