package com.winter.oauth2.configure;

import com.winter.oauth2.constant.OauthConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器配置
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/12/22 15:14
 */
@Configuration
@EnableResourceServer
public class WinterOauthResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(OauthConstant.RESOURCE_ID_OAPI).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/oapi/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oapi/**").authenticated();
    }
}
