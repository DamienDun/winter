package com.winter.oauth2.service.impl;

import com.winter.common.utils.AutoMapUtils;
import com.winter.common.utils.SecurityUtils;
import com.winter.common.utils.StringUtils;
import com.winter.oauth2.constant.OauthAuthGrantType;
import com.winter.oauth2.domain.OauthAccount;
import com.winter.oauth2.dto.input.OauthAccountInput;
import com.winter.oauth2.dto.input.OauthClientInput;
import com.winter.oauth2.service.IOauthAccountService;
import com.winter.oauth2.service.IOauthClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/13 15:36
 */
@Service
@RequiredArgsConstructor
public class OauthClientServiceImpl implements IOauthClientService {

    private final JdbcClientDetailsService jdbcClientDetailsService;
    private final IOauthAccountService oauthAccountService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(OauthClientInput input) {
        jdbcClientDetailsService.addClientDetails(input);
        if (input.getAuthorizedGrantTypes().contains(OauthAuthGrantType.CLIENT_CREDENTIALS) && StringUtils.isNotEmpty(input.getUsername())
                && StringUtils.isNotEmpty(input.getPassword())) {
            OauthAccountInput oauthAccountInput = AutoMapUtils.map(input, OauthAccountInput.class);
            oauthAccountInput.setPassword(SecurityUtils.encryptPassword(oauthAccountInput.getPassword()));
            oauthAccountService.add(oauthAccountInput);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OauthClientInput input) {
        jdbcClientDetailsService.updateClientDetails(input);
        if (input.getAuthorizedGrantTypes().contains(OauthAuthGrantType.CLIENT_CREDENTIALS) && StringUtils.isNotEmpty(input.getUsername())
                && StringUtils.isNotEmpty(input.getPassword())) {
            OauthAccountInput oauthAccountInput = AutoMapUtils.map(input, OauthAccountInput.class);
            oauthAccountInput.setPassword(SecurityUtils.encryptPassword(oauthAccountInput.getPassword()));
            OauthAccount oauthAccount = oauthAccountService.loadByClientId(input.getClientId());
            if (Objects.isNull(oauthAccount)) {
                oauthAccountService.add(oauthAccountInput);
            } else {
                oauthAccountInput.setId(oauthAccount.getId());
                oauthAccountService.update(oauthAccountInput);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(String clientId) {
        jdbcClientDetailsService.removeClientDetails(clientId);
        oauthAccountService.deleteByClientId(clientId);
    }

    @Override
    public OauthClientInput load(String clientId) {
        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);
        if (Objects.isNull(clientDetails)) {
            return null;
        }
        OauthClientInput oauthClient = AutoMapUtils.map(clientDetails, OauthClientInput.class);
        OauthAccount oauthAccount = oauthAccountService.loadByClientId(oauthClient.getClientId());
        if (Objects.nonNull(oauthAccount)) {
            AutoMapUtils.mapForLoad(oauthAccount, oauthClient);
        }
        return oauthClient;
    }
}
