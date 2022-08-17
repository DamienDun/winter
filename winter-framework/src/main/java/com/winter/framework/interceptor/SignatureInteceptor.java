package com.winter.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.winter.common.annotation.SignatureRequired;
import com.winter.common.core.domain.AjaxResult;
import com.winter.common.enums.SignatureHeaderEnum;
import com.winter.common.utils.sign.SignatureUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.winter.common.enums.SignatureHeaderEnum.*;

/**
 * @author Damien
 * @description 签名验证拦截器
 * @create 2022/8/9 14:10
 */
@Component
public abstract class SignatureInteceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod hm = (HandlerMethod) handler;
        SignatureRequired anno = hm.getMethodAnnotation(SignatureRequired.class);
        if (anno == null) {
            return true;
        }
        String appKey = request.getHeader(X_APPKEY.getName());
        String timeStamp = request.getHeader(X_TIMESTAMP.getName());
        String nonce = request.getHeader(X_NONCE.getName());
        String signature = request.getHeader(X_SIGNATURE.getName());

        long now = System.currentTimeMillis();

        //1.校验请求头参数
        for (SignatureHeaderEnum item : SignatureHeaderEnum.values()) {
            if (!StringUtils.hasText(request.getHeader(item.getName()))) {
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().write(JSON.toJSONString(AjaxResult.error(String.format("Header中缺少'%s'", item.getName()))));
                return false;
            }
        }
        //2.appKey是否合法
        String appSecret = this.getAppSecret(appKey);
        if (!StringUtils.hasText(appSecret)) {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("appKey无效")));
            return false;
        }
        //3.校验timestamp,请求是否超时
        if (now < Long.parseLong(timeStamp)) {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("x-timestamp数据无效,大于当前时间")));
            return false;
        }
        if ((now - Long.parseLong(timeStamp) > anno.invalid())) {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(AjaxResult.error(String.format("请求时间超过%d秒,请求失效", anno.invalid() / 1000))));
            return false;
        }
        //4.流水号长度
        if (anno.limitNonce() && nonce.length() < anno.nonceMinLength()) {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(AjaxResult.error(String.format("x-nonce长度最少为%d位", anno.nonceMinLength()))));
            return false;
        }
        //5.是否允许重复请求
        if (!anno.allowRepeat() && this.isRepeatSubmit(request, anno)) {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(AjaxResult.error(String.format("不允许重复提交,x-nonce=%s", nonce))));
            return false;
        }
        //6.签名是否正确
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(X_APPKEY.getName(), appKey);
        headerMap.put(X_TIMESTAMP.getName(), timeStamp);
        headerMap.put(X_NONCE.getName(), nonce);
        if (!SignatureUtil.build(headerMap, appSecret).equals(signature)) {
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(AjaxResult.error("签名无效")));
            return false;
        }
        this.passAfter(hm, request, anno);
        return true;
    }

    /**
     * 验证之后通过要处理的事情
     *
     * @param handlerMethod
     * @param request
     * @param anno
     */
    protected void passAfter(HandlerMethod handlerMethod, HttpServletRequest request, SignatureRequired anno) {

    }

    /**
     * 是否重复提交
     *
     * @param request
     * @param anno
     * @return
     */
    protected abstract boolean isRepeatSubmit(HttpServletRequest request, SignatureRequired anno);

    /**
     * 根据appkey获取appSecret
     *
     * @param appKey appKey
     * @return
     */
    protected abstract String getAppSecret(String appKey);
}
