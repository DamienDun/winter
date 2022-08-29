package com.winter.swagger.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/29 16:25
 */
@ConfigurationProperties(prefix = WinterSwaggerProperties.PREFIX)
@ToString(callSuper = true)
@Getter
@Setter
public class WinterSwaggerProperties implements Serializable {

    private static final long serialVersionUID = 3511675331392463340L;

    /**
     * 属性前缀
     */
    public final static String PREFIX = "swagger";

    private boolean enabled;

    private String pathMapping;
}
