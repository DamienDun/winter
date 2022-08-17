package com.winter.common.utils.sign;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.util.DigestUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Damien
 * @description Signature签名生成
 * @create 2022/8/9 13:20
 */
public class SignatureUtil {

    /**
     * 构建Signature签名
     *
     * @param appKey    申请得到的appKey
     * @param appSecret 申请得到的appSecret
     * @return
     */
    public static String build(String appKey, String appSecret) {
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("x-appKey", appKey);
        headerMap.put("x-nonce", nonce);
        headerMap.put("x-timestamp", timestamp);
        return DigestUtils.md5DigestAsHex((JSONObject.toJSONString(headerMap) + appSecret).getBytes());
    }


    /**
     * 构建Signature签名
     *
     * @param headerMap 请求头Map
     * @param appSecret 秘钥
     * @return
     */
    public static String build(Map<String, Object> headerMap, String appSecret) {
        return DigestUtils.md5DigestAsHex((JSONObject.toJSONString((headerMap) + appSecret).getBytes()));
    }

    public static void main(String[] args) {
        String appKey = "SOck2E83i4q";
        String appSecret = "Ab83LKeMs$EoEPeQXD81N";
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        System.out.println(timestamp);
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("x-appKey", appKey);
        headerMap.put("x-nonce", "1234567899");
        headerMap.put("x-timestamp", timestamp);
        System.out.println(build(headerMap, appSecret));
        System.out.println(build(appKey, appSecret));
    }

}
