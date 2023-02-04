package com.winter.oauth2.configure;

import com.winter.common.utils.AutoMapUtils;
import com.winter.oauth2.OauthAccountUserDetails;
import com.winter.oauth2.constant.OauthConstant;
import com.winter.oauth2.dto.AccountInfoVo;
import com.winter.oauth2.properties.WinterOauthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * oauth自动装配配置类
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/9 14:51
 */
@Configuration
@EnableConfigurationProperties({WinterOauthProperties.class})
@RequiredArgsConstructor
public class WinterOauthAutoConfiguration {

    private final DataSource dataSource;
    private final RedisConnectionFactory redisConnectionFactory;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 声明 ClientDetails实现
     *
     * @return ClientDetailsService
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(bCryptPasswordEncoder);
        return jdbcClientDetailsService;
    }

    /**
     * 自定义TokenEnhancerChain 由多个TokenEnhancer组成
     */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain(WinterOauthProperties properties) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        if (OauthConstant.JWT_TOKEN_STORE.equals(properties.getTokenStore())) {
            delegates.add(jwtAccessTokenConverter(properties));
        }
        delegates.add(extraInfoTokenEnhancer());
        tokenEnhancerChain.setTokenEnhancers(delegates);
        return tokenEnhancerChain;
    }

    /**
     * JWT 转换器
     */
    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter(WinterOauthProperties properties) {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(properties.getSigningKey());
        return converter;
    }

    /**
     * jwtToken存储
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(TokenStore.class)
    public TokenStore tokenStore(WinterOauthProperties properties) {
        if (OauthConstant.REDIS_TOKEN_STORE.equals(properties.getTokenStore())) {
            return new RedisTokenStore(redisConnectionFactory);
        }
        return new JwtTokenStore(jwtAccessTokenConverter(properties));
    }

    /**
     * tokenService 配置
     */
    @Bean(name = "tokenServices")
    public AuthorizationServerTokenServices tokenServices(WinterOauthProperties properties) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(jdbcClientDetailsService());
        // 允许支持refreshToken
        tokenServices.setSupportRefreshToken(properties.isSupportRefreshToken());
        // refreshToken 不重用策略
        tokenServices.setReuseRefreshToken(properties.isReuseRefreshToken());
        tokenServices.setRefreshTokenValiditySeconds(properties.getRefreshTokenValiditySeconds());
        tokenServices.setAccessTokenValiditySeconds(properties.getAccessTokenValiditySeconds());
        //设置Token存储方式
        tokenServices.setTokenStore(tokenStore(properties));
        tokenServices.setTokenEnhancer(tokenEnhancerChain(properties));
        return tokenServices;
    }

    /**
     * token 额外自定义信息 此例获取用户信息
     */
    @Bean
    public TokenEnhancer extraInfoTokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> information = new HashMap<>(8);
            Authentication userAuthentication = authentication.getUserAuthentication();
            if (userAuthentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) userAuthentication;
                Object principal = token.getPrincipal();
                if (principal instanceof OauthAccountUserDetails) {
                    OauthAccountUserDetails userDetails = (OauthAccountUserDetails) token.getPrincipal();
                    information.put(OauthConstant.EXTRA_INFO_ACCOUNT_INFO, AutoMapUtils.map(userDetails.getOauthAccount(), AccountInfoVo.class));
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(information);
                }
            }
            return accessToken;
        };
    }
}
