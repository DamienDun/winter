package com.winter.oauth2;

import com.alibaba.fastjson2.annotation.JSONField;
import com.winter.oauth2.domain.OauthAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * oauth账号用户details
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/9 14:53
 */
public class OauthAccountUserDetails implements UserDetails {

    private static final long serialVersionUID = 4592817400523063837L;

    private final OauthAccount oauthAccount;

    private final Collection<? extends GrantedAuthority> authorities;

    public OauthAccountUserDetails(OauthAccount oauthAccount, Collection<? extends GrantedAuthority> authorities) {
        this.oauthAccount = oauthAccount;
        this.authorities = authorities;
    }

    public OauthAccount getOauthAccount() {
        return oauthAccount;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return Objects.nonNull(oauthAccount) ? oauthAccount.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return Objects.nonNull(oauthAccount) ? oauthAccount.getUsername() : null;
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
