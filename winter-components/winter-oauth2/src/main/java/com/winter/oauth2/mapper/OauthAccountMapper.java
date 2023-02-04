package com.winter.oauth2.mapper;

import com.winter.common.core.mapper.DefaultBaseMapper;
import com.winter.oauth2.domain.OauthAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * oauth账号 Mapper
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2023/1/10 13:44
 */
@Mapper
public interface OauthAccountMapper extends DefaultBaseMapper<OauthAccount> {
}
