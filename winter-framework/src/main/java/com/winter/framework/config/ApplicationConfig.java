package com.winter.framework.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.winter.common.factory.TrimmedAnnotationFormatterFactory;
import com.winter.common.utils.json.DateDeserializer;
import com.winter.common.utils.json.DateSerializer;
import com.winter.common.utils.json.LocalDateTimeDeserializer;
import com.winter.common.utils.json.LocalDateTimeSerializer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

/**
 * 程序注解配置
 *
 * @author winter
 */
@Configuration
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.winter.**.mapper")
public class ApplicationConfig implements WebMvcConfigurer {

    /**
     * 时区配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
            //全局Long转String
            jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);

            //全局日期
            jacksonObjectMapperBuilder.serializerByType(Date.class, DateSerializer.INSTANCE);
            jacksonObjectMapperBuilder.deserializerByType(Date.class, DateDeserializer.INSTANCE);

            jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
            jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        };
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new TrimmedAnnotationFormatterFactory());
    }
}
