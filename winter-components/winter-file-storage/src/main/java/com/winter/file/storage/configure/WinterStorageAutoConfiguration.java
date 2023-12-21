package com.winter.file.storage.configure;

import com.winter.common.client.DistributionLockCli;
import com.winter.common.runtime.cache.ProxyCacheManager;
import com.winter.file.storage.StorageClient;
import com.winter.file.storage.StorageClientContext;
import com.winter.file.storage.clients.aliyun.AliyunStorageClient;
import com.winter.file.storage.clients.aliyun.AliyunStorageClientProperties;
import com.winter.file.storage.clients.fastdfs.FastDFSStorageClient;
import com.winter.file.storage.clients.fastdfs.FastDFSStorageClientProperties;
import com.winter.file.storage.clients.huawei.HuaWeiStorageClient;
import com.winter.file.storage.clients.huawei.HuaWeiStorageClientProperties;
import com.winter.file.storage.clients.minio.MinioStorageClient;
import com.winter.file.storage.clients.minio.MinioStorageClientProperties;
import com.winter.file.storage.clients.tencent.TencentStorageClient;
import com.winter.file.storage.clients.tencent.TencentStorageClientProperties;
import com.winter.file.storage.impl.StorageClientContextImpl;
import com.winter.file.storage.properties.WinterStorageProperties;
import com.winter.file.storage.service.BigFileUploadMinioService;
import com.winter.file.storage.service.FsBigFileUploadTaskService;
import com.winter.file.storage.service.impl.BigFileUploadMinioServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * winter 存储自动装配
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/15 19:24
 */
@Configuration
@EnableConfigurationProperties({WinterStorageProperties.class})
public class WinterStorageAutoConfiguration {

    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(WinterStorageAutoConfiguration.class);

    /**
     * 存储客户端上下文
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(StorageClientContext.class)
    public StorageClientContext storageClientContext() {
        return new StorageClientContextImpl();
    }

    /**
     * 阿里云 oos
     *
     * @param properties 属性
     * @return
     */
    @Bean(AliyunStorageClientProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = AliyunStorageClientProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(AliyunStorageClient.class)
    public StorageClient aliyunStorageClient(WinterStorageProperties properties) {
        return new AliyunStorageClient(properties.getAliyun());
    }

    /**
     * 华为云 obs
     *
     * @param properties 属性
     * @return
     */
    @Bean(HuaWeiStorageClientProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = HuaWeiStorageClientProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(HuaWeiStorageClient.class)
    public StorageClient huaWeiStorageClient(WinterStorageProperties properties) {
        return new HuaWeiStorageClient(properties.getHuawei());
    }

    /**
     * 腾讯云 cos
     *
     * @param properties 属性
     * @return
     */
    @Bean(TencentStorageClientProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = TencentStorageClientProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(TencentStorageClient.class)
    public StorageClient tencentStorageClient(WinterStorageProperties properties) {
        return new TencentStorageClient(properties.getTencent());
    }

    /**
     * FastDFS 客户端
     *
     * @param properties 属性
     * @return
     */
    @Bean(FastDFSStorageClientProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = FastDFSStorageClientProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(FastDFSStorageClient.class)
    public StorageClient fastDFSStorageClient(WinterStorageProperties properties) {
        FastDFSStorageClientProperties clientProperties = properties.getFastDFS();
        clientProperties.initByProperties();
        return new FastDFSStorageClient(clientProperties);
    }

    /**
     * Minio 客户端
     *
     * @param properties 属性
     * @return
     */
    @Bean(MinioStorageClientProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = MinioStorageClientProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(MinioStorageClient.class)
    public StorageClient minioStorageClient(WinterStorageProperties properties) {
        return new MinioStorageClient(properties.getMinio());
    }

    @Bean
    @ConditionalOnProperty(name = MinioStorageClientProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnBean({StorageClient.class, DistributionLockCli.class, ProxyCacheManager.class})
    @ConditionalOnMissingBean(BigFileUploadMinioService.class)
    public BigFileUploadMinioService bigFileUploadMinioService(WinterStorageProperties properties, DistributionLockCli distributionLockCli,
                                                               ProxyCacheManager proxyCacheManager, FsBigFileUploadTaskService fsBigFileUploadTaskService) {
        return new BigFileUploadMinioServiceImpl((MinioStorageClient) minioStorageClient(properties), distributionLockCli,
                proxyCacheManager, fsBigFileUploadTaskService, properties);
    }

    /**
     * 拦截
     *
     * @param clientContext 客户端上下文
     * @return
     */
    @Bean
    public BeanPostProcessor cellStorageChannelBeanPostProcessor(StorageClientContext clientContext) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof StorageClient) {
                    StorageClient client = (StorageClient) bean;
                    logger.info(client.toString());
                    clientContext.register(client);
                }
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }
        };
    }
}
