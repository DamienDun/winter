package com.winter.swagger.domain;

import lombok.Getter;
import lombok.Setter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.Serializable;

/**
 * 接口文档
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/29 15:04
 */
@Getter
@Setter
public class DocketInfo extends Docket implements Serializable {

    private static final long serialVersionUID = 3155943866419641759L;

    public DocketInfo(DocumentationType documentationType, String groupName) {
        super(documentationType);
        super.groupName(groupName);
    }
}
