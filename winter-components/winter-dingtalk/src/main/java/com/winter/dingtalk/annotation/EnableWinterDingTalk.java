package com.winter.dingtalk.annotation;

import com.winter.dingtalk.configure.WinterDingTalkAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用钉钉客户端
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/19 14:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({WinterDingTalkAutoConfiguration.class})
public @interface EnableWinterDingTalk {

}
