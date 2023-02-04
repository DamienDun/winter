package com.winter.oauth2.configure;

import com.winter.oauth2.service.impl.OauthAccountUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * oauth Security配置
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/12/22 15:31
 */
@Order(2)
@EnableWebSecurity
public class WinterOauthSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private OauthAccountUserDetailsServiceImpl oauthAccountUserDetailsService;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.requestMatchers()
                .antMatchers("/oauth/**", "/login-error")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin().loginPage("/login").failureUrl("/login-error");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(true);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProviderOauth() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(oauthAccountUserDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }

    @Bean
    public ProviderManager providerManager() {
        return new ProviderManager(daoAuthenticationProviderOauth());
    }

}
