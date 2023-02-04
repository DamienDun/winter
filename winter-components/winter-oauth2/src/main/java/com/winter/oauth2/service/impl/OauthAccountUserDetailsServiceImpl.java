package com.winter.oauth2.service.impl;

import com.winter.oauth2.OauthAccountUserDetails;
import com.winter.oauth2.domain.OauthAccount;
import com.winter.oauth2.service.IOauthAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/10 13:45
 */
@Service
@RequiredArgsConstructor
public class OauthAccountUserDetailsServiceImpl implements UserDetailsService {

    private final BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();
    private final IOauthAccountService oauthAccountService;
    private final JdbcClientDetailsService jdbcClientDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientId = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User clientUser = (User) principal;
                clientId = clientUser.getUsername();
            } else if (principal instanceof OauthAccountUserDetails) {
                getClientIdByRequest();
                return (OauthAccountUserDetails) principal;
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            clientId = getClientIdByRequest();
        }
        // 获取用户
        OauthAccount account = oauthAccountService.loadByClientIdAndUsername(clientId, username);
        // 授权
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new OauthAccountUserDetails(account, authorities);
    }

    /**
     * 从httpRequest中获取并验证客户端信息
     */
    public String getClientIdByRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new UnsupportedOperationException();
        }
        HttpServletRequest request = attributes.getRequest();
        UsernamePasswordAuthenticationToken client = authenticationConverter.convert(request);
        if (client == null) {
            throw new UnauthorizedClientException("unauthorized client");
        }
        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(client.getName());
        if (!passwordEncoder.matches((String) client.getCredentials(), clientDetails.getClientSecret())) {
            throw new BadClientCredentialsException();
        }
        return clientDetails.getClientId();
    }
}
