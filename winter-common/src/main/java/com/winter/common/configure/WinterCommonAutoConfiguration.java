package com.winter.common.configure;

import com.winter.common.runtime.cache.ProxyCacheManager;
import com.winter.common.runtime.cache.impl.ProxyCacheManagerImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/12/13 15:45
 */
@Configuration
public class WinterCommonAutoConfiguration {

    /**
     * 代理缓存
     *
     * @return
     */
    @Bean("proxyCacheManager")
    @Primary
    @ConditionalOnMissingBean(ProxyCacheManager.class)
    public ProxyCacheManager proxyCacheManager() {
        return new ProxyCacheManagerImpl();
    }

}
