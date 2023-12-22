package com.winter;

import com.github.yulichang.injector.MPJSqlInjector;
import com.winter.swagger.annotation.ApiGroup;
import com.winter.swagger.annotation.EnableWinterSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author winter
 */
@EnableWinterSwagger(
        groups = {
                @ApiGroup(groupName = "文件上传下载", packages = {"com.winter.web.controller.file"}),
        })
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MPJSqlInjector.class})
public class WinterApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(WinterApplication.class, args);
    }
}
