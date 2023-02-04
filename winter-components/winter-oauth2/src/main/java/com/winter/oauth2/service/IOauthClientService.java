package com.winter.oauth2.service;

import com.winter.oauth2.dto.input.OauthClientInput;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/13 15:35
 */
public interface IOauthClientService {

    /**
     * 新增客户端
     *
     * @param input
     */
    void add(OauthClientInput input);

    /**
     * 修改客户端
     *
     * @param input
     */
    void update(OauthClientInput input);

    /**
     * 删除客户端
     *
     * @param clientId 客户端id
     */
    void remove(String clientId);

    /**
     * 查询客户端
     *
     * @param clientId
     */
    OauthClientInput load(String clientId);
}
