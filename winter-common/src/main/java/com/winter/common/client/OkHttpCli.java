package com.winter.common.client;

import com.winter.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * OkHttp 请求客户端
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/19 10:16
 */
@Slf4j
@Component
public class OkHttpCli {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    @Autowired
    private OkHttpClient okHttpClient;

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        return okHttpClient.cookieJar().loadForRequest(httpUrl);
    }

    /**
     * 加载cookie
     *
     * @param httpUrl
     * @param cookies
     */
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        okHttpClient.cookieJar().saveFromResponse(httpUrl, cookies);
    }

    /**
     * get 请求
     *
     * @param url 请求url地址
     * @return string
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }


    /**
     * get 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGet(String url, String[] headers) {
        return doGet(url, null, headers);
    }


    /**
     * get 请求
     *
     * @param url     请求url地址
     * @param params  请求参数 map
     * @param headers 请求头字段 {k1, v1 k2, v2, ...}
     * @return string
     */
    public String doGet(String url, Map<String, String> params, String[] headers) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(params.get(key));
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(params.get(key));
                }
            }
        }

        Request.Builder builder = new Request.Builder();
        if (headers != null && headers.length > 0) {
            if (headers.length % 2 == 0) {
                for (int i = 0; i < headers.length; i = i + 2) {
                    builder.addHeader(headers[i], headers[i + 1]);
                }
            } else {
                log.warn("headers's length[{}] is error.", headers.length);
            }

        }

        Request request = builder.url(sb.toString()).build();
        log.info("do get request and url[{}]", sb.toString());
        return execute(request);
    }

    /**
     * post 请求
     *
     * @param url       请求url地址
     * @param params    请求参数 map
     * @param headerMap 请求头Map
     * @return string
     */
    public String doPost(String url, Map<String, Object> params, Map<String, String> headerMap) {
        FormBody.Builder builder = new FormBody.Builder();

        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, String.valueOf(params.get(key)));
            }
        }

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).post(builder.build());
        if (Objects.nonNull(headerMap) && headerMap.keySet().size() > 0) {
            headerMap.keySet().forEach(key -> requestBuilder.addHeader(key, headerMap.get(key)));
        }
        Request request = requestBuilder.build();
        log.info("do post request and url[{}]", url);

        return execute(request);
    }

    /**
     * post 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doPost(String url, Map<String, Object> params) {
        return doPost(url, params, null);
    }


    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url       请求url地址
     * @param json      请求数据, json 字符串
     * @param headerMap 请求头Map
     * @return string
     */
    public String doPostJson(String url, String json, Map<String, String> headerMap) {
        log.info("do post request and url[{}]", url);
        return exectePost(url, json, JSON, headerMap);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url  请求url地址
     * @param json 请求数据, json 字符串
     * @return string
     */
    public String doPostJson(String url, String json) {
        log.info("do post request and url[{}]", url);
        return exectePost(url, json, JSON, null);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url       请求url地址
     * @param xml       请求数据, xml 字符串
     * @param headerMap 请求头Map
     * @return string
     */
    public String doPostXml(String url, String xml, Map<String, String> headerMap) {
        log.info("do post request and url[{}]", url);
        return exectePost(url, xml, XML, headerMap);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @return string
     */
    public String doPostXml(String url, String xml) {
        log.info("do post request and url[{}]", url);
        return exectePost(url, xml, XML, null);
    }

    private String exectePost(String url, String data, MediaType contentType, Map<String, String> headerMap) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).post(requestBody);
        if (Objects.nonNull(headerMap) && headerMap.keySet().size() > 0) {
            headerMap.keySet().forEach(key -> requestBuilder.addHeader(key, headerMap.get(key)));
        }
        Request request = requestBuilder.build();
        return execute(request);
    }

    private String execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String respResult = response.body().string();
                log.info("response: {}", respResult);
                return respResult;
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new ServiceException(e.getMessage());
        }
        return "";
    }

}

