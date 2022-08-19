package com.winter.file.storage.annotation;

import com.winter.file.storage.configure.WinterStorageAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用存储
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/15 19:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({WinterStorageAutoConfiguration.class})
public @interface EnableWinterStorage {
}
