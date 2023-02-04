package com.winter.oauth2.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.winter.common.core.domain.entity.DefaultBaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * oauth账号 实体
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/9 14:55
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("oauth_account")
public class OauthAccount extends DefaultBaseEntity {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
