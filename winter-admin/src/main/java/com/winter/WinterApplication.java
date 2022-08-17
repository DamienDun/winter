package com.winter;

import com.github.yulichang.injector.MPJSqlInjector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author winter
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MPJSqlInjector.class})
public class WinterApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(WinterApplication.class, args);
    }
}
