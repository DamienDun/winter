package com.winter.dingtalk.clients;

import com.aliyun.dingtalkoauth2_1_0.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.winter.common.config.WinterConfig;
import com.winter.common.exception.ConfigureException;
import com.winter.common.exception.base.BaseException;
import com.winter.common.utils.ExceptionUtil;
import com.winter.common.utils.LocalCacheUtil;
import com.winter.dingtalk.properties.WinterDingtalkProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static com.winter.dingtalk.constants.DtAccessTokenTypeConstant.*;

/**
 * 抽象钉钉客户端
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/19 21:02
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractDtClient implements IDtClient {

    /**
     * accessToken 缓存key
     */
    public final static String ACCESS_TOKEN = WinterDingtalkProperties.PREFIX + ".accesstoken";

    /**
     * jsapiTicket 缓存key
     */
    public final static String JSAPI_TICKET = WinterDingtalkProperties.PREFIX + ".jsapiticket";

    /**
     * 钉钉属性
     */
    private WinterDingtalkProperties properties;

    private static com.aliyun.dingtalkoauth2_1_0.Client client;

    static {
        try {
            client = new com.aliyun.dingtalkoauth2_1_0.Client(new Config().setProtocol("https").setRegionId("central"));
        } catch (Exception e) {
            log.error("dingtalkoauth2_1_0 client create fail");
        }
    }

    public AbstractDtClient(WinterDingtalkProperties properties) {
        this.properties = properties;
    }

    /**
     * 根据钉钉配置属性获取凭证
     *
     * @return
     */
    @Override
    public String getAccessToken() {
        switch (properties.getAccessTokenType().toLowerCase()) {
            case INTERNAL_APP:
                return getInternalAppAccessToken();
            case CORP:
                return getCorpAccessToken();
            case SUITE:
                return getSuiteAccessToken();
            case SSO:
                return getSsoAccessToken();
            default:
                throw new ConfigureException(WinterConfig.PREFIX + ".dingtalk.accessTokenType 非法配置");
        }
    }

    /**
     * 创建jsapi ticket
     *
     * @return
     */
    @Override
    public String getJsapiTicket() {
        ExceptionUtil.checkNotNull(properties, "钉钉配置属性不能为空");
        Object cacheObj = LocalCacheUtil.get(JSAPI_TICKET);
        if (cacheObj != null) {
            return String.valueOf(cacheObj);
        }
        CreateJsapiTicketHeaders createJsapiTicketHeaders = new CreateJsapiTicketHeaders();
        createJsapiTicketHeaders.xAcsDingtalkAccessToken = getAccessToken();
        String jsapiTicket = null;
        try {
            CreateJsapiTicketResponse response = client.createJsapiTicketWithOptions(createJsapiTicketHeaders, new RuntimeOptions());
            jsapiTicket = response.getBody().getJsapiTicket();
            LocalCacheUtil.set(JSAPI_TICKET, jsapiTicket, response.getBody().getExpireIn() * 1000);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("创建jsapi ticket失败");
        }
        return jsapiTicket;
    }

    /**
     * 获取企业内部应用的accessToken
     *
     * @return
     */
    public String getInternalAppAccessToken() {
        ExceptionUtil.checkNotNull(properties, "钉钉配置属性不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getAppKey(), "dingtalk.appKey不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getAppSecret(), "dingtalk.appSecret不能为空");
        String cacheKey = WinterDingtalkProperties.PREFIX + "." + INTERNAL_APP;
        Object cacheObj = LocalCacheUtil.get(cacheKey);
        if (cacheObj != null) {
            return String.valueOf(cacheObj);
        }
        GetAccessTokenRequest request = new GetAccessTokenRequest()
                .setAppKey(properties.getAppKey())
                .setAppSecret(properties.getAppSecret());
        try {
            GetAccessTokenResponseBody body = client.getAccessToken(request).getBody();
            LocalCacheUtil.set(cacheKey, body.getAccessToken(), body.getExpireIn() * 1000);
            return body.getAccessToken();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("获取企业内部应用的accessToken失败");
        }
    }

    /**
     * 获取第三方应用授权企业的accessToken
     *
     * @return
     */
    public String getCorpAccessToken() {
        ExceptionUtil.checkNotNull(properties, "钉钉配置属性不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getAppKey(), "dingtalk.appKey不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getAppSecret(), "dingtalk.appSecret不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getCorpId(), "dingtalk.corpId不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getSuiteTicket(), "dingtalk.suiteTicket不能为空");
        String cacheKey = WinterDingtalkProperties.PREFIX + "." + CORP;
        Object cacheObj = LocalCacheUtil.get(cacheKey);
        if (cacheObj != null) {
            return String.valueOf(cacheObj);
        }
        GetCorpAccessTokenRequest request = new GetCorpAccessTokenRequest()
                .setSuiteKey(this.properties.getAppKey())
                .setSuiteSecret(this.properties.getAppSecret())
                .setAuthCorpId(this.properties.getCorpId())
                .setSuiteTicket(this.properties.getSuiteTicket());
        try {
            GetCorpAccessTokenResponseBody body = client.getCorpAccessToken(request).getBody();
            LocalCacheUtil.set(cacheKey, body.getAccessToken(), body.getExpireIn() * 1000);
            return body.getAccessToken();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("获取第三方应用授权企业的accessToken失败");
        }
    }

    /**
     * 获取第三方企业应用的suiteAccessToken
     *
     * @return
     */
    public String getSuiteAccessToken() {
        ExceptionUtil.checkNotNull(properties, "钉钉配置属性不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getSuiteKey(), "suiteKey不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getSuiteSecret(), "suiteSecret不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getSuiteTicket(), "suiteTicket不能为空");
        String cacheKey = WinterDingtalkProperties.PREFIX + "." + SUITE;
        Object cacheObj = LocalCacheUtil.get(cacheKey);
        if (cacheObj != null) {
            return String.valueOf(cacheObj);
        }
        GetSuiteAccessTokenRequest getSuiteAccessTokenRequest = new GetSuiteAccessTokenRequest()
                .setSuiteKey(properties.getSuiteKey())
                .setSuiteSecret(properties.getSuiteSecret())
                .setSuiteTicket(properties.getSuiteTicket());
        try {
            GetSuiteAccessTokenResponseBody body = client.getSuiteAccessToken(getSuiteAccessTokenRequest).getBody();
            LocalCacheUtil.set(cacheKey, body.getAccessToken(), body.getExpireIn() * 1000);
            return body.getAccessToken();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("获取第三方企业应用的suiteAccessToken失败");
        }
    }


    /**
     * 获取微应用后台免登的access_token
     *
     * @return
     */
    public String getSsoAccessToken() {
        ExceptionUtil.checkNotNull(properties, "钉钉配置属性不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getCorpId(), "dingtalk.corpId不能为空");
        ExceptionUtil.checkNotNullOrBlank(properties.getSsoSecret(), "dingtalk.ssoSecret不能为空");
        String cacheKey = WinterDingtalkProperties.PREFIX + "." + SSO;
        Object cacheObj = LocalCacheUtil.get(cacheKey);
        if (cacheObj != null) {
            return String.valueOf(cacheObj);
        }
        GetSsoAccessTokenRequest request = new GetSsoAccessTokenRequest()
                .setCorpid(properties.getCorpId())
                .setSsoSecret(properties.getSsoSecret());
        try {
            GetSsoAccessTokenResponseBody body = client.getSsoAccessToken(request).getBody();
            LocalCacheUtil.set(cacheKey, body.getAccessToken(), body.getExpireIn() * 1000);
            return body.getAccessToken();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("获取微应用后台免登的access_token失败");
        }
    }
}
