package com.winter.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.winter.common.core.service.BaseService;
import com.winter.common.utils.StringUtils;
import com.winter.oauth2.domain.OauthAccount;
import com.winter.oauth2.dto.input.OauthAccountInput;
import com.winter.oauth2.mapper.OauthAccountMapper;
import com.winter.oauth2.service.IOauthAccountService;
import org.springframework.stereotype.Service;

/**
 * oauth账号 Service
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/10 13:50
 */
@Service
public class OauthAccountServiceImpl extends BaseService<Long, OauthAccount, OauthAccountMapper,
        OauthAccountInput, OauthAccountInput> implements IOauthAccountService {

    @Override
    public String getModuleName() {
        return "oauth认证模块";
    }

    @Override
    public OauthAccount loadByClientIdAndUsername(String clientId, String username) {
        if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(username)) {
            return null;
        }
        LambdaQueryWrapper<OauthAccount> wrapper = Wrappers.lambdaQuery(OauthAccount.class)
                .eq(OauthAccount::getClientId, clientId)
                .eq(OauthAccount::getUsername, username);
        return getMapper().selectForFirst(wrapper);
    }

    @Override
    public OauthAccount loadByClientId(String clientId) {
        if (StringUtils.isEmpty(clientId)) {
            return null;
        }
        LambdaQueryWrapper<OauthAccount> wrapper = Wrappers.lambdaQuery(OauthAccount.class)
                .eq(OauthAccount::getClientId, clientId);
        return getMapper().selectForFirst(wrapper);
    }

    @Override
    public void deleteByClientId(String clientId) {
        deleteByWrapper(Wrappers.lambdaQuery(OauthAccount.class).eq(OauthAccount::getClientId, clientId));
    }
}
