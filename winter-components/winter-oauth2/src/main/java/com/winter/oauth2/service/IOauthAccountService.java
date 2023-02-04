package com.winter.oauth2.service;

import com.winter.common.core.service.IBaseService;
import com.winter.oauth2.domain.OauthAccount;
import com.winter.oauth2.dto.input.OauthAccountInput;

/**
 * oauth认证 Service
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/10 13:47
 */
public interface IOauthAccountService extends IBaseService<Long, OauthAccountInput, OauthAccountInput> {

    /**
     * 根据客户端id以及用户名加载
     *
     * @param clientId
     * @param username
     * @return
     */
    OauthAccount loadByClientIdAndUsername(String clientId, String username);

    /**
     * 根据客户端id查询
     *
     * @param clientId
     * @return
     */
    OauthAccount loadByClientId(String clientId);

    /**
     * 根据客户端id删除
     *
     * @param clientId
     */
    void deleteByClientId(String clientId);
}
